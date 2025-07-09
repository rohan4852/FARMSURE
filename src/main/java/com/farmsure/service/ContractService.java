package com.farmsure.service;

import com.farmsure.model.Contract;
import com.farmsure.model.User;
import com.farmsure.repository.ContractRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ContractService {
    @Autowired
    private ContractRepository contractRepository;

    @Transactional
    public Contract createContract(Contract contract) {
        return contractRepository.save(contract);
    }

    public List<Contract> findByMerchant(User merchant) {
        return contractRepository.findByMerchant(merchant);
    }

    public List<Contract> getContractsByMerchant(Long merchantId) {
        User merchant = new User();
        merchant.setId(merchantId);
        return contractRepository.findByMerchant(merchant);
    }

    public List<Contract> findByAssignedFarmer(User farmer) {
        return contractRepository.findByAssignedFarmer(farmer);
    }

    public List<Contract> findByStatus(String status) {
        return contractRepository.findByStatus(status);
    }

    public List<Contract> findByMerchantAndStatus(User merchant, String status) {
        return contractRepository.findByMerchantAndStatus(merchant, status);
    }

    public Contract getContractById(Long id) {
        return contractRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contract not found"));
    }

    @Transactional
    public void deleteContract(Long id) {
        contractRepository.deleteById(id);
    }

    public List<Contract> findAll() {
        return contractRepository.findAll();
    }

    public List<Contract> findRecentContractsByFarmer(User farmer) {
        // Return the 5 most recent contracts for the given farmer
        List<Contract> contracts = contractRepository.findByAssignedFarmerOrderByCreatedAtDesc(farmer);
        return contracts.size() > 5 ? contracts.subList(0, 5) : contracts;
    }

    public List<Contract> findByAssignedFarmerAndStatus(User farmer, String status) {
        return contractRepository.findByAssignedFarmerAndStatus(farmer, status);
    }
}
