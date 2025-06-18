package com.farmsure.service;

import com.farmsure.model.Transaction;
import com.farmsure.model.User;
import com.farmsure.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction addTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getTransactionsByFarmer(User farmer) {
        return transactionRepository.findByFarmer(farmer);
    }

    public List<Transaction> getTransactionsByMerchant(User merchant) {
        return transactionRepository.findByMerchant(merchant);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public double getMonthlyRevenueByFarmer(User farmer) {
        List<Transaction> transactions = transactionRepository.findByFarmer(farmer);
        double total = 0;
        java.time.YearMonth currentMonth = java.time.YearMonth.now();
        for (Transaction t : transactions) {
            java.time.LocalDate date = t.getTransactionDate().toLocalDate();
            if (java.time.YearMonth.from(date).equals(currentMonth)) {
                total += t.getPrice() * t.getQuantity();
            }
        }
        return total;
    }
}
