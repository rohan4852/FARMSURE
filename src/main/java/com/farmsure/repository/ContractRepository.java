package com.farmsure.repository;

import com.farmsure.model.Contract;
import com.farmsure.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {
    List<Contract> findByMerchant(User merchant);

    List<Contract> findByMerchantOrderByCreatedAtDesc(User merchant);

    List<Contract> findByAssignedFarmer(User farmer);

    List<Contract> findByAssignedFarmerOrderByCreatedAtDesc(User farmer);

    List<Contract> findByStatus(String status);

    List<Contract> findByMerchantAndStatus(User merchant, String status);
}
