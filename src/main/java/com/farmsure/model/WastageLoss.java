package com.farmsure.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "wastage_loss")
public class WastageLoss {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inventory_item_id", nullable = false)
    private InventoryItem inventoryItem;

    @Column(name = "wastage_quantity", nullable = false)
    private Double wastageQuantity;

    @Column(name = "loss_quantity", nullable = false)
    private Double lossQuantity;

    @Column(name = "recorded_date", nullable = false)
    private LocalDateTime recordedDate;

    @Column(name = "description")
    private String description;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
        if (recordedDate == null) {
            recordedDate = createdAt;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InventoryItem getInventoryItem() {
        return inventoryItem;
    }

    public void setInventoryItem(InventoryItem inventoryItem) {
        this.inventoryItem = inventoryItem;
    }

    public Double getWastageQuantity() {
        return wastageQuantity;
    }

    public void setWastageQuantity(Double wastageQuantity) {
        this.wastageQuantity = wastageQuantity;
    }

    public Double getLossQuantity() {
        return lossQuantity;
    }

    public void setLossQuantity(Double lossQuantity) {
        this.lossQuantity = lossQuantity;
    }

    public LocalDateTime getRecordedDate() {
        return recordedDate;
    }

    public void setRecordedDate(LocalDateTime recordedDate) {
        this.recordedDate = recordedDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
