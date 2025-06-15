package com.farmsure.repository;

import com.farmsure.model.PriceOffer;
import com.farmsure.model.Product;
import com.farmsure.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PriceOfferRepository extends JpaRepository<PriceOffer, Long> {
    List<PriceOffer> findByProduct(Product product);

    List<PriceOffer> findByMerchant(User merchant);
}
