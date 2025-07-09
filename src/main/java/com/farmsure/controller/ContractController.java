package com.farmsure.controller;

import com.farmsure.model.Contract;
import com.farmsure.model.User;
import com.farmsure.model.Bid;
import com.farmsure.service.ContractService;
import com.farmsure.service.UserService;
import com.farmsure.service.BidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping("/contracts")
public class ContractController {
    private static final Logger logger = LoggerFactory.getLogger(ContractController.class);

    @Autowired
    private ContractService contractService;

    @Autowired
    private UserService userService;

    @Autowired
    private BidService bidService;

    @GetMapping
    public String listContracts(@AuthenticationPrincipal User user, Model model) {
        List<Contract> contracts;
        if ("ROLE_MERCHANT".equals(user.getRole())) {
            contracts = contractService.findByMerchant(user);
        } else if ("ROLE_FARMER".equals(user.getRole())) {
            // Show open contracts available for bidding to farmers
            contracts = contractService.findByStatus("OPEN");
        } else {
            // Admin sees all contracts
            contracts = contractService.findAll();
        }
        model.addAttribute("contracts", contracts);
        return "dashboard/contracts/list";
    }

    @GetMapping("/{id}/download-agreement")
    public void downloadContractAgreement(@PathVariable Long id, @AuthenticationPrincipal User user,
            HttpServletResponse response) {
        Contract contract = contractService.getContractById(id);
        if (contract == null) {
            throw new RuntimeException("Contract not found");
        }
        // Authorization check: only merchant or assigned farmer can download
        if (!contract.getMerchant().equals(user) &&
                (contract.getAssignedFarmer() == null || !contract.getAssignedFarmer().equals(user))) {
            throw new RuntimeException("Unauthorized");
        }

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=contract_agreement_" + id + ".pdf");

        try (OutputStream out = response.getOutputStream()) {
            // Generate PDF using iText
            com.itextpdf.kernel.pdf.PdfWriter writer = new com.itextpdf.kernel.pdf.PdfWriter(out);
            com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
            com.itextpdf.layout.Document document = new com.itextpdf.layout.Document(pdfDoc);

            document.add(new com.itextpdf.layout.element.Paragraph("Contract Agreement").setBold().setFontSize(18));
            document.add(new com.itextpdf.layout.element.Paragraph("Contract ID: " + contract.getId()));
            document.add(new com.itextpdf.layout.element.Paragraph("Product: " + contract.getProduct()));
            document.add(new com.itextpdf.layout.element.Paragraph("Quantity: " + contract.getQuantity()));
            document.add(new com.itextpdf.layout.element.Paragraph("Status: " + contract.getStatus()));
            document.add(new com.itextpdf.layout.element.Paragraph("Base Price: " + contract.getBasePrice()));
            document.add(new com.itextpdf.layout.element.Paragraph("Delivery Date: " + contract.getDeliveryDate()));

            document.add(new com.itextpdf.layout.element.Paragraph("Merchant Details:"));
            document.add(new com.itextpdf.layout.element.Paragraph("Name: " + contract.getMerchant().getFullName()));
            document.add(new com.itextpdf.layout.element.Paragraph("Email: " + contract.getMerchant().getEmail()));
            document.add(new com.itextpdf.layout.element.Paragraph("Phone: " + contract.getMerchant().getPhone()));

            document.add(new com.itextpdf.layout.element.Paragraph("Farmer Details:"));
            if (contract.getAssignedFarmer() != null) {
                document.add(new com.itextpdf.layout.element.Paragraph(
                        "Name: " + contract.getAssignedFarmer().getFullName()));
                document.add(
                        new com.itextpdf.layout.element.Paragraph("Email: " + contract.getAssignedFarmer().getEmail()));
                document.add(
                        new com.itextpdf.layout.element.Paragraph("Phone: " + contract.getAssignedFarmer().getPhone()));
            } else {
                document.add(new com.itextpdf.layout.element.Paragraph("Not assigned"));
            }

            document.add(new com.itextpdf.layout.element.Paragraph("Terms:"));
            document.add(new com.itextpdf.layout.element.Paragraph(contract.getTerms()));

            document.add(new com.itextpdf.layout.element.Paragraph(
                    "Fixed Amount to be Paid to Farmer: " + contract.getBasePrice()));

            document.add(
                    new com.itextpdf.layout.element.Paragraph("Deadline of Delivery: " + contract.getDeliveryDate()));

            // Placeholder for signatures
            document.add(new com.itextpdf.layout.element.Paragraph("\n\nSignatures:\n\n"));
            document.add(new com.itextpdf.layout.element.Paragraph("Merchant Signature: ____________________"));
            document.add(new com.itextpdf.layout.element.Paragraph("Farmer Signature: ____________________"));

            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    @GetMapping({ "/new", "/add" })
    public String newContract(Model model) {
        model.addAttribute("contract", new Contract());
        return "dashboard/contracts/form";
    }

    @PostMapping
    public String createContract(@AuthenticationPrincipal User user, @Valid @ModelAttribute Contract contract,
            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors()
                    .forEach(error -> System.out.println("[CONTRACT FORM ERROR] " + error.toString()));
            model.addAttribute("errorMessage", "Please correct the errors in the form.");
            return "dashboard/contracts/form";
        }
        contract.setMerchant(user);
        contract.setStatus("OPEN");
        contractService.createContract(contract);
        model.addAttribute("successMessage", "Contract created successfully!");
        return "redirect:/contracts";
    }

    @GetMapping("/{id:\\d+}")
    public String viewContract(@PathVariable Long id, Model model) {
        model.addAttribute("contract", contractService.getContractById(id));
        return "dashboard/contracts/view";
    }

    @GetMapping("/{id}/bid")
    public String showBidForm(@PathVariable Long id, Model model) {
        Contract contract = contractService.getContractById(id);
        model.addAttribute("contract", contract);
        model.addAttribute("bid", new Bid());
        return "dashboard/contracts/bid";
    }

    @PostMapping("/{id}/bid")
    public String placeBid(@PathVariable Long id,
            @AuthenticationPrincipal User user,
            @ModelAttribute Bid bid) {
        Contract contract = contractService.getContractById(id);
        bidService.placeBid(contract, user, bid.getAmount(), bid.getProposal());
        return "redirect:/contracts";
    }

    @GetMapping("/{id}/bids")
    public String viewBids(@PathVariable Long id, Model model) {
        Contract contract = contractService.getContractById(id);
        model.addAttribute("contract", contract);
        model.addAttribute("bids", bidService.findByContract(contract));
        return "dashboard/contracts/bids";
    }

    @PostMapping("/{contractId}/bids/{bidId}/accept")
    public String acceptBid(@PathVariable Long contractId,
            @PathVariable Long bidId,
            @AuthenticationPrincipal User user) {
        Contract contract = contractService.getContractById(contractId);
        if (!contract.getMerchant().equals(user)) {
            throw new RuntimeException("Unauthorized");
        }
        bidService.acceptBid(contractId, bidId);
        return "redirect:/contracts/" + contractId + "/bids";
    }

    @PostMapping("/{contractId}/bids/{bidId}/decline")
    public String declineBid(@PathVariable Long contractId,
            @PathVariable Long bidId,
            @AuthenticationPrincipal User user) {
        Contract contract = contractService.getContractById(contractId);
        if (!contract.getMerchant().equals(user)) {
            throw new RuntimeException("Unauthorized");
        }
        bidService.declineBid(contractId, bidId);
        return "redirect:/contracts/" + contractId + "/bids";
    }

    @DeleteMapping("/{id:\\d+}")
    public String deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return "redirect:/contracts";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        logger.error("[CONTRACTS] Unhandled exception: ", ex);
        model.addAttribute("errorMessage", ex.getMessage());
        // Add stack trace for debugging
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : ex.getStackTrace()) {
            sb.append(element.toString()).append("\n");
        }
        model.addAttribute("stackTrace", sb.toString());
        return "error/error";
    }

    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Test OK";
    }

    @GetMapping("/test-endpoint")
    @ResponseBody
    public String testEndpoint() {
        return "Test OK";
    }
}
