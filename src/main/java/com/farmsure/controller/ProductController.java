package com.farmsure.controller;

import com.farmsure.model.Product;
import com.farmsure.model.User;
import com.farmsure.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/farmer")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/add-crop")
    public String showAddCropForm(Model model) {
        logger.info("Accessing /farmer/add-crop page");
        model.addAttribute("product", new Product());
        return "dashboard/farmer/add_product";
    }

    @PostMapping("/add-crop")
    public String addCrop(@ModelAttribute("product") Product product, Authentication authentication) {
        User farmer = (User) authentication.getPrincipal();
        logger.info("Adding crop for farmer: " + farmer.getUsername());
        product.setFarmer(farmer);
        productService.addProduct(product);
        return "redirect:/farmer/my-crops";
    }

    @GetMapping("/my-crops")
    public String viewMyCrops(Model model, Authentication authentication) {
        User farmer = (User) authentication.getPrincipal();
        logger.info("Viewing crops for farmer: " + farmer.getUsername());
        model.addAttribute("products", productService.getProductsByFarmer(farmer));
        return "farmer/my-crops";
    }
}
