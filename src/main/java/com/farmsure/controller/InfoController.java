package com.farmsure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/info")
public class InfoController {
    @GetMapping("/about")
    public String about(Model model) {
        return "info/about";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        return "info/contact";
    }

    @GetMapping("/privacy")
    public String privacy(Model model) {
        return "info/privacy";
    }
}
