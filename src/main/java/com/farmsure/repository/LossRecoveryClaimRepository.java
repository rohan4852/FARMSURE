package com.farmsure.repository;

import com.farmsure.model.LossRecoveryClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LossRecoveryClaimRepository extends JpaRepository<LossRecoveryClaim, Long> {
    // Additional query methods can be added here if needed
}
