

package com.farmsure.controller;

import com.farmsure.model.LossRecoveryClaim;
import com.farmsure.model.User;
import com.farmsure.service.LossRecoveryClaimService;
import com.farmsure.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final LossRecoveryClaimService lossRecoveryClaimService;

    @Autowired
    public AdminController(UserService userService, LossRecoveryClaimService lossRecoveryClaimService) {
        this.userService = userService;
        this.lossRecoveryClaimService = lossRecoveryClaimService;
    }

    @GetMapping("/users")
    public String manageUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/users/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/users/edit/{id}")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        model.addAttribute("user", user);
        return "admin/edit-user";
    }

    @PostMapping("/users/edit")
    public String editUserSubmit(@ModelAttribute User user) {
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        // Add attributes for contracts and platform activity overview
        return "admin/dashboard";
    }

    @GetMapping("/loss-recovery")
    public String viewLossRecoveryClaims(Model model) {
        model.addAttribute("claims", lossRecoveryClaimService.getAllClaims());
        return "admin/loss-recovery";
    }

    @GetMapping("/loss-recovery/approve/{id}")
    public String approveClaim(@PathVariable Long id) {
        LossRecoveryClaim claim = lossRecoveryClaimService.getClaimById(id);
        if (claim != null) {
            claim.setStatus("Approved");
            lossRecoveryClaimService.saveClaim(claim);
        }
        return "redirect:/admin/loss-recovery";
    }

    @GetMapping("/loss-recovery/reject/{id}")
    public String rejectClaim(@PathVariable Long id) {
        LossRecoveryClaim claim = lossRecoveryClaimService.getClaimById(id);
        if (claim != null) {
            claim.setStatus("Rejected");
            lossRecoveryClaimService.saveClaim(claim);
        }
        return "redirect:/admin/loss-recovery";
    }
}
