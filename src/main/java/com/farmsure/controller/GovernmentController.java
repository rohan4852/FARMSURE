package com.farmsure.controller;

import com.farmsure.model.BasePrice;
import com.farmsure.model.Transaction;
import com.farmsure.service.BasePriceService;
import com.farmsure.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/government")
public class GovernmentController {

    private final BasePriceService basePriceService;
    private final TransactionService transactionService;

    @Autowired
    public GovernmentController(BasePriceService basePriceService, TransactionService transactionService) {
        this.basePriceService = basePriceService;
        this.transactionService = transactionService;
    }

    @GetMapping("/base-prices")
    public String listBasePrices(Model model) {
        List<BasePrice> basePrices = basePriceService.getAllBasePrices();
        model.addAttribute("basePrices", basePrices);
        return "government/base_prices";
    }

    @GetMapping("/base-prices/new")
    public String showAddBasePriceForm(Model model) {
        model.addAttribute("basePrice", new BasePrice());
        return "government/add_base_price";
    }

    @PostMapping("/base-prices")
    public String addBasePrice(@ModelAttribute BasePrice basePrice) {
        basePriceService.addBasePrice(basePrice);
        return "redirect:/government/base-prices";
    }

    @GetMapping("/transactions")
    public String listTransactions(Model model) {
        List<Transaction> transactions = transactionService.getAllTransactions();
        model.addAttribute("transactions", transactions);
        return "government/transactions";
    }
}
