package com.farmsure.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyCropsController {

    @GetMapping("/my-crops")
    public String myCrops(Model model) {
        // Add model attributes as needed
        return "dashboard/farmer/my_crops";
    }
}
