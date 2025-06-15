package com.farmsure.service;

import com.farmsure.model.BasePrice;
import com.farmsure.repository.BasePriceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BasePriceService {
    private final BasePriceRepository basePriceRepository;

    @Autowired
    public BasePriceService(BasePriceRepository basePriceRepository) {
        this.basePriceRepository = basePriceRepository;
    }

    public BasePrice addBasePrice(BasePrice basePrice) {
        return basePriceRepository.save(basePrice);
    }

    public List<BasePrice> getAllBasePrices() {
        return basePriceRepository.findAll();
    }

    public BasePrice getBasePriceById(Long id) {
        return basePriceRepository.findById(id).orElse(null);
    }
}
