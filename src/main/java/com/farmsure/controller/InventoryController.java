package com.farmsure.controller;

import com.farmsure.model.Contract;
import com.farmsure.model.InventoryItem;
import com.farmsure.model.User;
import com.farmsure.service.ContractService;
import com.farmsure.service.InventoryService;
import com.farmsure.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService inventoryService;
    private final ContractService contractService;
    private final UserService userService;

    @Autowired
    public InventoryController(InventoryService inventoryService, ContractService contractService,
            UserService userService) {
        this.inventoryService = inventoryService;
        this.contractService = contractService;
        this.userService = userService;
    }

    @GetMapping
    public String inventory(@AuthenticationPrincipal User currentUser, Model model) {
        List<InventoryItem> inventoryItems = inventoryService.getInventoryItemsByMerchant(currentUser.getId());
        model.addAttribute("inventoryItems", inventoryItems);
        return "dashboard/merchant/inventory";
    }

    @GetMapping("/add")
    public String showAddForm(@AuthenticationPrincipal User currentUser, Model model) {
        InventoryItem inventoryItem = new InventoryItem();
        model.addAttribute("inventoryItem", inventoryItem);

        // Get contracts assigned to current merchant
        List<Contract> contracts = contractService.getContractsByMerchant(currentUser.getId());
        model.addAttribute("contracts", contracts);

        // Get farmers assigned to these contracts
        List<User> farmers = userService.getFarmersByContracts(contracts);
        model.addAttribute("farmers", farmers);

        return "dashboard/merchant/inventory_form";
    }

    @PostMapping("/add")
    public String addInventoryItem(@AuthenticationPrincipal User currentUser,
            @Valid @ModelAttribute("inventoryItem") InventoryItem inventoryItem,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            // Reload contracts and farmers for form
            List<Contract> contracts = contractService.getContractsByMerchant(currentUser.getId());
            model.addAttribute("contracts", contracts);
            List<User> farmers = userService.getFarmersByContracts(contracts);
            model.addAttribute("farmers", farmers);
            return "dashboard/merchant/inventory_form";
        }
        inventoryService.saveInventoryItem(inventoryItem);
        return "redirect:/inventory";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, @AuthenticationPrincipal User currentUser, Model model) {
        InventoryItem inventoryItem = inventoryService.getInventoryItemById(id);
        if (inventoryItem == null) {
            return "redirect:/inventory";
        }
        model.addAttribute("inventoryItem", inventoryItem);

        List<Contract> contracts = contractService.getContractsByMerchant(currentUser.getId());
        model.addAttribute("contracts", contracts);

        List<User> farmers = userService.getFarmersByContracts(contracts);
        model.addAttribute("farmers", farmers);

        return "dashboard/merchant/inventory_form";
    }

    @PostMapping("/edit/{id}")
    public String editInventoryItem(@PathVariable Long id,
            @AuthenticationPrincipal User currentUser,
            @Valid @ModelAttribute("inventoryItem") InventoryItem inventoryItem,
            BindingResult bindingResult,
            Model model) {
        if (bindingResult.hasErrors()) {
            List<Contract> contracts = contractService.getContractsByMerchant(currentUser.getId());
            model.addAttribute("contracts", contracts);
            List<User> farmers = userService.getFarmersByContracts(contracts);
            model.addAttribute("farmers", farmers);
            return "dashboard/merchant/inventory_form";
        }
        inventoryItem.setId(id);
        inventoryService.saveInventoryItem(inventoryItem);
        return "redirect:/inventory";
    }

    @PostMapping("/delete/{id}")
    public String deleteInventoryItem(@PathVariable Long id) {
        inventoryService.deleteInventoryItem(id);
        return "redirect:/inventory";
    }
}
