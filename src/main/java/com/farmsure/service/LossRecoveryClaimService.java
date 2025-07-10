package com.farmsure.service;

import com.farmsure.model.LossRecoveryClaim;
import com.farmsure.repository.LossRecoveryClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LossRecoveryClaimService {

    private final LossRecoveryClaimRepository lossRecoveryClaimRepository;

    @Autowired
    public LossRecoveryClaimService(LossRecoveryClaimRepository lossRecoveryClaimRepository) {
        this.lossRecoveryClaimRepository = lossRecoveryClaimRepository;
    }

    public LossRecoveryClaim saveClaim(LossRecoveryClaim claim) {
        return lossRecoveryClaimRepository.save(claim);
    }

    public List<LossRecoveryClaim> getAllClaims() {
        return lossRecoveryClaimRepository.findAll();
    }

    public LossRecoveryClaim getClaimById(Long id) {
        return lossRecoveryClaimRepository.findById(id).orElse(null);
    }

    public void deleteClaim(Long id) {
        lossRecoveryClaimRepository.deleteById(id);
    }
}
