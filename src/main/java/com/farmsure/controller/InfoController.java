package com.farmsure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InfoController {

    @GetMapping("/about")
    public String about(Model model) {
        return "dashboard/about";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        return "dashboard/contact";
    }

    @GetMapping("/privacy")
    public String privacy(Model model) {
        return "dashboard/privacy";
    }
}
