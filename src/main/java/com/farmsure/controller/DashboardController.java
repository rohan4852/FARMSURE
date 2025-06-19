package com.farmsure.controller;

import com.farmsure.model.User;
import com.farmsure.service.ContractService;
import com.farmsure.service.UserService;
import com.farmsure.service.BidService;
import com.farmsure.service.TransactionService;
import com.farmsure.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DashboardController {

    @Autowired
    private ContractService contractService;

    @Autowired
    private UserService userService;

    @Autowired
    private BidService bidService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private ProductService productService;

    @GetMapping("/dashboard")
    public String dashboard(@RequestParam(required = false) String type, Model model, Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName());
            if (user == null) {
                return "error/404";
            }
            model.addAttribute("username", user.getUsername());

            // Determine the user's role and redirect to appropriate dashboard
            if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_FARMER"))) {
                // Add farmer-specific data
                var activeContracts = contractService.findByAssignedFarmer(user);
                model.addAttribute("activeContracts",
                        activeContracts != null ? activeContracts : java.util.Collections.emptyList());

                var availableContracts = contractService.findByStatus("OPEN");
                model.addAttribute("availableContracts",
                        availableContracts != null ? availableContracts : java.util.Collections.emptyList());

                var myBids = bidService.findByFarmer(user);
                model.addAttribute("myBids", myBids != null ? myBids : java.util.Collections.emptyList());

                // Add revenue (monthly)
                double revenue = transactionService.getMonthlyRevenueByFarmer(user);
                model.addAttribute("monthlyRevenue", revenue);

                // Add products listed count
                var products = productService.getProductsByFarmer(user);
                model.addAttribute("productsCount", products != null ? products.size() : 0);

                // Add pending bids count
                long pendingBidsCount = 0;
                if (myBids != null) {
                    pendingBidsCount = myBids.stream()
                            .filter(bid -> "PENDING".equalsIgnoreCase(bid.getStatus()))
                            .count();
                }
                model.addAttribute("pendingBidsCount", pendingBidsCount);

                return "dashboard/farmer/index";
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_MERCHANT"))) {
                // Add merchant-specific data
                model.addAttribute("myContracts", contractService.findByMerchant(user));
                model.addAttribute("pendingBids", bidService.findPendingByMerchant(user));
                model.addAttribute("activeContracts", contractService.findByMerchantAndStatus(user, "ASSIGNED"));
                return "dashboard/merchant/index";
            } else if (authentication.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                return "dashboard/admin/index";
            }

            return "dashboard/index";
        } catch (Exception e) {
            model.addAttribute("statusCode", 500);
            model.addAttribute("errorTitle", "Internal Server Error");
            model.addAttribute("errorMessage", e.getMessage());
            return "error/error";
        }
    }

    @GetMapping("/farmer/active-contracts")
    public String farmerActiveContracts(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("username", user.getUsername());

        // Get all open contracts available for bidding
        model.addAttribute("availableContracts", contractService.findByStatus("OPEN"));

        return "dashboard/farmer/active-contracts";
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        model.addAttribute("username", authentication.getName());
        model.addAttribute("user", user);
        return "dashboard/profile";
    }

    @GetMapping("/settings")
    public String settings(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        return "dashboard/settings";
    }

    @GetMapping("/marketplace")
    @PreAuthorize("hasAnyRole('FARMER', 'MERCHANT')")
    public String marketplace(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        return "dashboard/marketplace";
    }

    @GetMapping("/contracts-overview")
    @PreAuthorize("hasAnyRole('FARMER', 'MERCHANT')")
    public String contracts(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        return "dashboard/contracts-overview";
    }

    @GetMapping("/messages")
    public String messages(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        return "dashboard/messages";
    }

    @GetMapping("/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public String reports(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        return "dashboard/reports";
    }
}
