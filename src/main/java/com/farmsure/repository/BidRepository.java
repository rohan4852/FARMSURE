package com.farmsure.repository;

import com.farmsure.model.Bid;
import com.farmsure.model.Contract;
import com.farmsure.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidRepository extends JpaRepository<Bid, Long> {
    List<Bid> findByContract(Contract contract);

    List<Bid> findByContractOrderByAmountAsc(Contract contract);

    List<Bid> findByFarmer(User farmer);

    List<Bid> findByFarmerOrderByCreatedAtDesc(User farmer);

    boolean existsByContractAndFarmer(Contract contract, User farmer);

    List<Bid> findByContractAndStatus(Contract contract, String status);

    List<Bid> findByContractMerchantAndStatus(User merchant, String status);

    List<Bid> findByStatus(String status);
}
