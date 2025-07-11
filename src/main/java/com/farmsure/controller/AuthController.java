package com.farmsure.controller;

import com.farmsure.model.User;
import com.farmsure.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            if ("ROLE_FARMER".equals(role)) {
                return "redirect:/dashboard?type=farmer";
            } else if ("ROLE_MERCHANT".equals(role)) {
                return "redirect:/dashboard?type=merchant";
            } else if ("ROLE_ADMIN".equals(role)) {
                return "redirect:/admin/dashboard";
            }
        }
        return "redirect:/register";
    }

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user, RedirectAttributes redirectAttributes) {
        try {
            if (user.getRole() == null || user.getRole().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Please select a role");
                return "redirect:/register";
            }

            userService.registerUser(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful. Please login.");
            return "redirect:/login";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Registration failed: " + e.getMessage());
            return "redirect:/register";
        }
    }
}
