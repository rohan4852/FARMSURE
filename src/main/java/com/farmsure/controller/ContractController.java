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

import java.util.List;

@Controller
@RequestMapping("/contracts")
public class ContractController {
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
            contracts = contractService.findByAssignedFarmer(user);
        } else {
            // Admin sees all contracts
            contracts = contractService.findAll();
        }
        model.addAttribute("contracts", contracts);
        return "dashboard/contracts/list";
    }

    @GetMapping("/new")
    public String newContract(Model model) {
        model.addAttribute("contract", new Contract());
        return "dashboard/contracts/form";
    }

    @PostMapping
    public String createContract(@AuthenticationPrincipal User user, @ModelAttribute Contract contract) {
        contract.setMerchant(user);
        contract.setStatus("OPEN");
        contractService.createContract(contract);
        return "redirect:/contracts";
    }

    @GetMapping("/{id}")
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

    @DeleteMapping("/{id}")
    public String deleteContract(@PathVariable Long id) {
        contractService.deleteContract(id);
        return "redirect:/contracts";
    }
}
