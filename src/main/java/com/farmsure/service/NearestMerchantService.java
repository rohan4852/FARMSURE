package com.farmsure.service;

import com.farmsure.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NearestMerchantService {

    // Dummy implementation of KNN algorithm for nearest merchant search
    // In real scenario, merchant locations and distance calculations would be used

    public List<User> findNearestMerchants(User farmer, List<User> allMerchants, int k) {
        // For simplicity, return first k merchants
        return allMerchants.stream().limit(k).collect(Collectors.toList());
    }
}
