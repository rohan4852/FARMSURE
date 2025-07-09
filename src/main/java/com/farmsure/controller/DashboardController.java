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
import org.springframework.web.bind.annotation.PathVariable;
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
    public String dashboardRedirect(@RequestParam(name = "type", required = false) String type) {
        if ("farmer".equalsIgnoreCase(type)) {
            return "redirect:/dashboard/farmer/index";
        } else if ("merchant".equalsIgnoreCase(type)) {
            return "redirect:/dashboard/merchant/index";
        } else {
            return "redirect:/"; // or some default page
        }
    }

    @GetMapping("/dashboard/farmer/index")
    public String farmerDashboard(Model model, Authentication authentication) {
        try {
            java.util.Optional<User> optionalUser = userService.findByUsername(authentication.getName());
            if (optionalUser.isEmpty()) {
                return "error/404";
            }
            User user = optionalUser.get();
            model.addAttribute("username", user.getUsername());

            // Get all contracts where this farmer has placed a bid
            var myBids = bidService.findByFarmer(user);
            java.util.Set<Long> contractIds = new java.util.HashSet<>();
            java.util.Map<Long, com.farmsure.model.Bid> bidMap = new java.util.HashMap<>();
            if (myBids != null) {
                for (var bid : myBids) {
                    if (bid.getContract() != null && bid.getContract().getId() != null) {
                        contractIds.add(bid.getContract().getId());
                        bidMap.put(bid.getContract().getId(), bid);
                    }
                }
            }

            var allContracts = contractService.findByStatus("OPEN");
            java.util.List<com.farmsure.model.Contract> placedBidContracts = new java.util.ArrayList<>();
            if (allContracts != null) {
                for (var contract : allContracts) {
                    if (contract != null && contract.getId() != null && contractIds.contains(contract.getId())) {
                        placedBidContracts.add(contract);
                    }
                }
            }

            // Also add contracts assigned to this farmer (accepted contracts)
            var assignedContracts = contractService.findByAssignedFarmer(user);
            if (assignedContracts != null) {
                for (var contract : assignedContracts) {
                    if (!placedBidContracts.contains(contract)) {
                        placedBidContracts.add(contract);
                    }
                }
            }

            // Calculate total revenue for farmer (sum of basePrice * quantity for assigned
            // contracts)
            double totalRevenue = 0.0;
            if (assignedContracts != null) {
                for (var contract : assignedContracts) {
                    if (contract.getBasePrice() != null && contract.getQuantity() != null) {
                        totalRevenue += contract.getBasePrice() * contract.getQuantity();
                    }
                }
            }

            // Count active merchants (distinct merchants in assigned contracts)
            java.util.Set<User> activeMerchantsSet = new java.util.HashSet<>();
            if (assignedContracts != null) {
                for (var contract : assignedContracts) {
                    if (contract.getMerchant() != null) {
                        activeMerchantsSet.add(contract.getMerchant());
                    }
                }
            }

            model.addAttribute("activeContracts", placedBidContracts);
            model.addAttribute("bidMap", bidMap);
            model.addAttribute("totalRevenue", totalRevenue);
            model.addAttribute("activeMerchants", activeMerchantsSet.size());
            model.addAttribute("totalBids", myBids != null ? myBids.size() : 0);

            // Add null checks for activeContracts and bidMap in model to avoid Thymeleaf
            // errors
            if (placedBidContracts == null) {
                model.addAttribute("activeContracts", java.util.Collections.emptyList());
            }
            if (bidMap == null) {
                model.addAttribute("bidMap", java.util.Collections.emptyMap());
            }

            return "dashboard/farmer/index";
        } catch (Exception e) {
            model.addAttribute("statusCode", 500);
            model.addAttribute("errorTitle", "Internal Server Error");
            model.addAttribute("errorMessage", e.getMessage());
            return "error/error";
        }
    }

    @GetMapping("/dashboard/merchant/index")
    public String merchantDashboard(Model model, Authentication authentication) {
        try {
            java.util.Optional<User> optionalUser = userService.findByUsername(authentication.getName());
            if (optionalUser.isEmpty()) {
                return "error/404";
            }
            User user = optionalUser.get();
            model.addAttribute("username", user.getUsername());

            // Fetch active contracts for this merchant
            var activeContracts = contractService.findByMerchant(user);
            model.addAttribute("activeContracts",
                    activeContracts != null ? activeContracts : java.util.Collections.emptyList());

            // Calculate total investment (sum of contract base prices * quantities)
            double totalInvestment = 0.0;
            if (activeContracts != null) {
                for (var contract : activeContracts) {
                    if (contract.getBasePrice() != null && contract.getQuantity() != null) {
                        totalInvestment += contract.getBasePrice() * contract.getQuantity();
                    }
                }
            }
            model.addAttribute("totalInvestment", totalInvestment);

            // Count active farmers (distinct assigned farmers in active contracts)
            java.util.Set<User> activeFarmersSet = new java.util.HashSet<>();
            if (activeContracts != null) {
                for (var contract : activeContracts) {
                    if (contract.getAssignedFarmer() != null) {
                        activeFarmersSet.add(contract.getAssignedFarmer());
                    }
                }
            }
            model.addAttribute("activeFarmers", activeFarmersSet.size());

            // Count active bids (bids placed on merchant's contracts with status 'ACTIVE'
            // or similar)
            int activeBidsCount = 0;
            if (activeContracts != null) {
                for (var contract : activeContracts) {
                    var bids = bidService.findByContract(contract);
                    if (bids != null) {
                        for (var bid : bids) {
                            if ("ACTIVE".equalsIgnoreCase(bid.getStatus())) {
                                activeBidsCount++;
                            }
                        }
                    }
                }
            }
            model.addAttribute("activeBids", activeBidsCount);

            // Fetch pending bids (bids with status 'PENDING' on merchant's contracts)
            java.util.List<com.farmsure.model.Bid> pendingBids = new java.util.ArrayList<>();
            if (activeContracts != null) {
                for (var contract : activeContracts) {
                    var bids = bidService.findByContract(contract);
                    if (bids != null) {
                        for (var bid : bids) {
                            if ("PENDING".equalsIgnoreCase(bid.getStatus()) && bid.getFarmer() != null) {
                                pendingBids.add(bid);
                            }
                        }
                    }
                }
            }
            model.addAttribute("pendingBids", pendingBids);

            return "dashboard/merchant/index";
        } catch (Exception e) {
            model.addAttribute("statusCode", 500);
            model.addAttribute("errorTitle", "Internal Server Error");
            model.addAttribute("errorMessage", e.getMessage());
            return "error/error";
        }
    }

    @GetMapping("/contracts/{id}/view")
    @PreAuthorize("hasAnyRole('FARMER', 'MERCHANT')")
    public String viewContract(@PathVariable("id") Long id, Model model, Authentication authentication) {
        java.util.Optional<User> optionalUser = userService.findByUsername(authentication.getName());
        if (optionalUser.isEmpty()) {
            return "error/404";
        }
        User user = optionalUser.get();
        var contract = contractService.getContractById(id);
        if (contract == null) {
            model.addAttribute("statusCode", 404);
            model.addAttribute("errorTitle", "Contract Not Found");
            model.addAttribute("errorMessage", "The requested contract does not exist.");
            return "error/error";
        }
        model.addAttribute("contract", contract);
        model.addAttribute("username", user.getUsername());
        return "dashboard/contracts/view";
    }

    @GetMapping("/farmer/active-contracts")
    @PreAuthorize("hasRole('FARMER')")
    public String farmerActiveContracts(Model model, Authentication authentication) {
        try {
            java.util.Optional<User> optionalUser = userService.findByUsername(authentication.getName());
            if (optionalUser.isEmpty()) {
                return "error/404";
            }
            User user = optionalUser.get();
            model.addAttribute("username", user.getUsername());

            // Get all contracts where this farmer has placed a bid
            var myBids = bidService.findByFarmer(user);
            java.util.Set<Long> contractIds = new java.util.HashSet<>();
            java.util.Map<Long, com.farmsure.model.Bid> bidMap = new java.util.HashMap<>();
            if (myBids != null) {
                for (var bid : myBids) {
                    if (bid.getContract() != null && bid.getContract().getId() != null) {
                        contractIds.add(bid.getContract().getId());
                        bidMap.put(bid.getContract().getId(), bid);
                    }
                }
            }

            var allContracts = contractService.findByStatus("OPEN");
            java.util.List<com.farmsure.model.Contract> placedBidContracts = new java.util.ArrayList<>();
            if (allContracts != null) {
                for (var contract : allContracts) {
                    if (contract != null && contract.getId() != null && contractIds.contains(contract.getId())) {
                        placedBidContracts.add(contract);
                    }
                }
            }

            model.addAttribute("activeContracts", placedBidContracts);
            model.addAttribute("bidMap", bidMap);
            return "dashboard/farmer/active-contracts";
        } catch (Exception e) {
            model.addAttribute("statusCode", 500);
            model.addAttribute("errorTitle", "Internal Server Error");
            model.addAttribute("errorMessage", e.getMessage());
            return "error/error";
        }
    }

    @GetMapping("/profile")
    public String profile(Model model, Authentication authentication) {
        try {
            java.util.Optional<User> optionalUser = userService.findByUsername(authentication.getName());
            if (optionalUser.isEmpty()) {
                return "error/404";
            }
            User user = optionalUser.get();
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
            java.util.Optional<User> optionalUser = userService.findByUsername(authentication.getName());
            if (optionalUser.isEmpty()) {
                return "error/404";
            }
            User user = optionalUser.get();
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
            java.util.Optional<User> optionalUser = userService.findByUsername(authentication.getName());
            if (optionalUser.isEmpty()) {
                return "error/404";
            }
            User user = optionalUser.get();
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
            java.util.Optional<User> optionalUser = userService.findByUsername(authentication.getName());
            if (optionalUser.isEmpty()) {
                return "error/404";
            }
            User user = optionalUser.get();
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
        java.util.Optional<User> optionalUser = userService.findByUsername(authentication.getName());
        if (optionalUser.isEmpty()) {
            return "error/404";
        }
        User user = optionalUser.get();
        String userRole = "";
        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MERCHANT"))) {
            userRole = "ROLE_MERCHANT";
            model.addAttribute("contracts", contractService.findByMerchant(user));
            model.addAttribute("placedBidContractIds", new java.util.HashSet<Long>());
        } else if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_FARMER"))) {
            userRole = "ROLE_FARMER";
            // For farmers, show all open contracts, but mark those where the user has
            // placed a bid
            var contracts = contractService.findByStatus("OPEN");
            var myBids = bidService.findByFarmer(user);
            java.util.Set<Long> placedBidContractIds = new java.util.HashSet<>();
            if (myBids != null) {
                for (var bid : myBids) {
                    placedBidContractIds.add(bid.getContract().getId());
                }
            }
            model.addAttribute("contracts", contracts != null ? contracts : java.util.Collections.emptyList());
            model.addAttribute("placedBidContractIds", placedBidContractIds);
        } else {
            model.addAttribute("contracts", java.util.Collections.emptyList());
        }
        model.addAttribute("userRole", userRole);
        return "dashboard/contracts-overview";
    }

    @GetMapping("/messages")
    public String messages(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        // Redirect to chat or inbox or remove this method if unused
        return "redirect:/messages/inbox";
    }

    @GetMapping("/dashboard/reports")
    @PreAuthorize("hasRole('ADMIN')")
    public String reports(Model model, Authentication authentication) {
        model.addAttribute("username", authentication.getName());
        return "dashboard/reports";
    }
}
