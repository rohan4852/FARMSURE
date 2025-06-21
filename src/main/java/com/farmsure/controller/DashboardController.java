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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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
                var myContracts = contractService.findByMerchant(user);
                var activeContracts = contractService.findByMerchantAndStatus(user, "ASSIGNED");
                var pendingBids = bidService.findPendingByMerchant(user);
                var allBids = bidService.findByMerchant(user);
                int activeFarmers = (int) myContracts.stream().filter(c -> c.getAssignedFarmer() != null)
                        .map(c -> c.getAssignedFarmer().getId()).distinct().count();
                double totalInvestment = myContracts.stream()
                        .mapToDouble(c -> c.getBasePrice() != null
                                ? c.getBasePrice() * (c.getQuantity() != null ? c.getQuantity() : 0)
                                : 0)
                        .sum();
                int activeBids = (allBids instanceof java.util.Collection) ? ((java.util.Collection<?>) allBids).size()
                        : 0;
                model.addAttribute("myContracts", myContracts);
                model.addAttribute("activeContracts", activeContracts);
                model.addAttribute("pendingBids", pendingBids);
                model.addAttribute("activeFarmers", activeFarmers);
                model.addAttribute("totalInvestment", totalInvestment);
                model.addAttribute("activeBids", activeBids);
                // TODO: Add real messages if needed
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
        try {
            User user = userService.findByUsername(authentication.getName());
            model.addAttribute("username", authentication.getName());
            model.addAttribute("user", user);
            return "dashboard/profile";
        } catch (Exception e) {
            model.addAttribute("statusCode", 500);
            model.addAttribute("errorTitle", "Error Loading Profile");
            model.addAttribute("errorMessage", "Could not load user profile. Please try again later.");
            return "error/error";
        }
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute("user") User updatedUser, Authentication authentication, Model model) {
        try {
            User user = userService.findByUsername(authentication.getName());
            user.setEmail(updatedUser.getEmail());
            user.setFullName(updatedUser.getFullName());
            user.setPhone(updatedUser.getPhone());
            user.setAddress(updatedUser.getAddress());
            userService.save(user);
            model.addAttribute("successMessage", "Profile updated successfully!");
            return "redirect:/profile";
        } catch (Exception e) {
            model.addAttribute("statusCode", 500);
            model.addAttribute("errorTitle", "Error Updating Profile");
            model.addAttribute("errorMessage", "Could not update profile. Please try again later.");
            return "error/error";
        }
    }

    @GetMapping("/settings")
    public String settings(Model model, Authentication authentication) {
        try {
            User user = userService.findByUsername(authentication.getName());
            model.addAttribute("username", authentication.getName());
            model.addAttribute("user", user);
            return "dashboard/settings";
        } catch (Exception e) {
            model.addAttribute("statusCode", 500);
            model.addAttribute("errorTitle", "Error Loading Settings");
            model.addAttribute("errorMessage", "Could not load user settings. Please try again later.");
            return "error/error";
        }
    }

    @PostMapping("/settings/update")
    public String updateSettings(
            @RequestParam("themePreference") String themePreference,
            @RequestParam("languagePreference") String languagePreference,
            @RequestParam(value = "emailNotifications", required = false) Boolean emailNotifications,
            @RequestParam(value = "smsNotifications", required = false) Boolean smsNotifications,
            Authentication authentication,
            Model model) {
        try {
            User user = userService.findByUsername(authentication.getName());
            user.setThemePreference(themePreference);
            user.setLanguagePreference(languagePreference);
            user.setEmailNotifications(emailNotifications != null ? emailNotifications : false);
            user.setSmsNotifications(smsNotifications != null ? smsNotifications : false);
            userService.save(user);
            model.addAttribute("successMessage", "Settings updated successfully!");
            return "redirect:/settings";
        } catch (Exception e) {
            model.addAttribute("statusCode", 500);
            model.addAttribute("errorTitle", "Error Updating Settings");
            model.addAttribute("errorMessage", "Could not update settings. Please try again later.");
            return "error/error";
        }
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
