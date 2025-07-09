package com.farmsure.service;

import com.farmsure.model.InventoryItem;
import com.farmsure.repository.InventoryItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryItemRepository inventoryItemRepository;

    @Autowired
    public InventoryService(InventoryItemRepository inventoryItemRepository) {
        this.inventoryItemRepository = inventoryItemRepository;
    }

    public List<InventoryItem> getInventoryItemsByMerchant(Long merchantId) {
        return inventoryItemRepository.findByContract_MerchantId(merchantId);
    }

    public InventoryItem saveInventoryItem(InventoryItem item) {
        return inventoryItemRepository.save(item);
    }

    public InventoryItem getInventoryItemById(Long id) {
        return inventoryItemRepository.findById(id).orElse(null);
    }

    public void deleteInventoryItem(Long id) {
        inventoryItemRepository.deleteById(id);
    }
}
