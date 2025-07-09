package com.farmsure.service;

import com.farmsure.model.InventoryItem;
import com.farmsure.model.User;
import com.farmsure.repository.InventoryItemRepository;
import com.farmsure.repository.WastageLossRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ReportService {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private WastageLossRepository wastageLossRepository;

    // Input usage per farmer (sum of inventory quantities grouped by farmer)
    public Map<User, Double> getInputUsagePerFarmer() {
        List<InventoryItem> items = inventoryItemRepository.findAll();
        return items.stream()
                .collect(Collectors.groupingBy(InventoryItem::getFarmer,
                        Collectors.summingDouble(item -> item.getQuantity() != null ? item.getQuantity() : 0)));
    }

    // Inventory valuation (sum of quantity * estimated price per inventory item)
    public double getInventoryValuation() {
        List<InventoryItem> items = inventoryItemRepository.findAll();
        // For simplicity, assume base price is stored in contract basePrice
        return items.stream()
                .mapToDouble(item -> {
                    double quantity = item.getQuantity() != null ? item.getQuantity() : 0;
                    double price = 0;
                    if (item.getContract() != null && item.getContract().getBasePrice() != null) {
                        price = item.getContract().getBasePrice();
                    }
                    return quantity * price;
                }).sum();
    }

    // Wastage or loss tracking (sum of wastage and loss quantities)
    public double getTotalWastage() {
        return wastageLossRepository.sumWastageQuantity();
    }

    public double getTotalLoss() {
        return wastageLossRepository.sumLossQuantity();
    }

    // Additional methods for analytics and forecasting can be added here
}
