package com.farmsure.controller;

import com.farmsure.model.Product;
import com.farmsure.model.PriceOffer;
import com.farmsure.model.User;
import com.farmsure.service.ProductService;
import com.farmsure.service.PriceOfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/farmer")
public class FarmerController {

    private final ProductService productService;
    private final PriceOfferService priceOfferService;

    @Autowired
    public FarmerController(ProductService productService, PriceOfferService priceOfferService) {
        this.productService = productService;
        this.priceOfferService = priceOfferService;
    }

    @GetMapping("/products")
    public String listProducts(@AuthenticationPrincipal User farmer, Model model) {
        List<Product> products = productService.getProductsByFarmer(farmer);
        model.addAttribute("products", products);
        return "farmer/products";
    }

    @GetMapping("/products/new")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "farmer/add_product";
    }

    @PostMapping("/products")
    public String addProduct(@AuthenticationPrincipal User farmer, @ModelAttribute Product product) {
        product.setFarmer(farmer);
        productService.addProduct(product);
        return "redirect:/farmer/products";
    }

    @GetMapping("/products/{productId}/prices")
    public String viewMerchantPrices(@PathVariable Long productId, Model model) {
        Product product = productService.getProductById(productId);
        if (product == null) {
            return "redirect:/farmer/products";
        }
        List<PriceOffer> priceOffers = priceOfferService.getPriceOffersByProduct(product);
        model.addAttribute("product", product);
        model.addAttribute("priceOffers", priceOffers);
        return "farmer/merchant_prices";
    }
}
