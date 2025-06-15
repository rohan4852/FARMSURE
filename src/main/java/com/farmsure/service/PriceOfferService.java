package com.farmsure.service;

import com.farmsure.model.PriceOffer;
import com.farmsure.model.Product;
import com.farmsure.model.User;
import com.farmsure.repository.PriceOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceOfferService {
    private final PriceOfferRepository priceOfferRepository;

    @Autowired
    public PriceOfferService(PriceOfferRepository priceOfferRepository) {
        this.priceOfferRepository = priceOfferRepository;
    }

    public PriceOffer addPriceOffer(PriceOffer priceOffer) {
        return priceOfferRepository.save(priceOffer);
    }

    public List<PriceOffer> getPriceOffersByProduct(Product product) {
        return priceOfferRepository.findByProduct(product);
    }

    public List<PriceOffer> getPriceOffersByMerchant(User merchant) {
        return priceOfferRepository.findByMerchant(merchant);
    }

    public List<PriceOffer> getAllPriceOffers() {
        return priceOfferRepository.findAll();
    }
}
