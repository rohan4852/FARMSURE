package com.farmsure.controller;

import com.farmsure.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize("hasRole('MERCHANT')")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/reports")
    public String reportsHome(Model model) {
        model.addAttribute("inputUsagePerFarmer", reportService.getInputUsagePerFarmer());
        model.addAttribute("inventoryValuation", reportService.getInventoryValuation());
        model.addAttribute("totalWastage", reportService.getTotalWastage());
        model.addAttribute("totalLoss", reportService.getTotalLoss());
        // Additional analytics data can be added here
        return "dashboard/reports/index";
    }
}
