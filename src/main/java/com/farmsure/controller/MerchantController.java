package com.farmsure.controller;

import com.farmsure.model.PriceOffer;
import com.farmsure.model.Product;
import com.farmsure.model.User;
import com.farmsure.service.PriceOfferService;
import com.farmsure.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/merchant")
public class MerchantController {

    private final PriceOfferService priceOfferService;
    private final ProductService productService;

    @Autowired
    public MerchantController(PriceOfferService priceOfferService, ProductService productService) {
        this.priceOfferService = priceOfferService;
        this.productService = productService;
    }

    @GetMapping("/products")
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "merchant/products";
    }

    @GetMapping("/products/{productId}/offer")
    public String showPriceOfferForm(@PathVariable Long productId, Model model) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return "redirect:/merchant/products";
        }
        model.addAttribute("product", product);
        model.addAttribute("priceOffer", new PriceOffer());
        return "merchant/offer_price";
    }

    @PostMapping("/products/{productId}/offer")
    public String submitPriceOffer(@PathVariable Long productId,
            @AuthenticationPrincipal User merchant,
            @ModelAttribute PriceOffer priceOffer,
            Model model) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return "redirect:/merchant/products";
        }
        // Validate price against government base price (to be implemented)
        priceOffer.setMerchant(merchant);
        priceOffer.setProduct(product);
        priceOfferService.addPriceOffer(priceOffer);
        return "redirect:/merchant/products";
    }
}
