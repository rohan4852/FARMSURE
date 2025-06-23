package com.farmsure.service;

import com.farmsure.model.Bid;
import com.farmsure.model.Contract;
import com.farmsure.model.User;
import com.farmsure.repository.BidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class BidService {
    @Autowired
    private BidRepository bidRepository;

    public Bid createBid(Bid bid) {
        if (bidRepository.existsByContractAndFarmer(bid.getContract(), bid.getFarmer())) {
            throw new RuntimeException("You have already placed a bid on this contract");
        }
        return bidRepository.save(bid);
    }

    public List<Bid> getContractBids(Contract contract) {
        return bidRepository.findByContract(contract);
    }

    public List<Bid> getFarmerBids(User farmer) {
        return bidRepository.findByFarmer(farmer);
    }

    public void deleteBid(Long id) {
        bidRepository.deleteById(id);
    }

    public void placeBid(Contract contract, User user, double amount, String proposal) {
        if (bidRepository.existsByContractAndFarmer(contract, user)) {
            throw new RuntimeException("You have already placed a bid on this contract");
        }
        Bid bid = new Bid();
        bid.setContract(contract);
        bid.setFarmer(user);
        bid.setAmount(amount);
        bid.setProposal(proposal);
        bidRepository.save(bid);
    }

    public List<Bid> getBidsForContract(Contract contract) {
        return bidRepository.findByContract(contract);
    }

    public List<Bid> findPendingByMerchant(User merchant) {
        return bidRepository.findByContractMerchantAndStatus(merchant, "PENDING");
    }

    public List<Bid> findByFarmer(User farmer) {
        return bidRepository.findByFarmer(farmer);
    }

    public List<Bid> findByContract(Contract contract) {
        return bidRepository.findByContract(contract);
    }

    public List<Bid> findByMerchant(User merchant) {
        // Find all bids for contracts owned by this merchant
        return bidRepository.findAll().stream()
                .filter(bid -> bid.getContract() != null && bid.getContract().getMerchant() != null
                        && bid.getContract().getMerchant().getId().equals(merchant.getId()))
                .toList();
    }

    @Transactional
    public void acceptBid(Long contractId, Long bidId) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));

        Contract contract = bid.getContract();
        contract.setStatus("ASSIGNED");
        contract.setAssignedFarmer(bid.getFarmer());

        bid.setStatus("ACCEPTED");
        bidRepository.save(bid);

        // Reject other bids
        bidRepository.findByContract(contract).stream()
                .filter(b -> !b.getId().equals(bidId))
                .forEach(b -> {
                    b.setStatus("REJECTED");
                    bidRepository.save(b);
                });
    }

    @Transactional
    public void declineBid(Long contractId, Long bidId) {
        Bid bid = bidRepository.findById(bidId)
                .orElseThrow(() -> new RuntimeException("Bid not found"));
        if (!bid.getContract().getId().equals(contractId)) {
            throw new RuntimeException("Bid does not belong to this contract");
        }
        bid.setStatus("REJECTED");
        bidRepository.save(bid);
    }
}
