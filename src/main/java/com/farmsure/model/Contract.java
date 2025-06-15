package com.farmsure.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Entity
@Table(name = "contracts")
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String product;

    @Column(nullable = false)
    private Double quantity;

    @Column(nullable = false)
    private Double basePrice;

    @Column(nullable = false)
    private String qualityGrade;

    @Column(nullable = false)
    private LocalDateTime biddingEndDate;

    @Column(nullable = false)
    private LocalDateTime deliveryDate;

    @Column(columnDefinition = "TEXT")
    private String terms;

    @Column(nullable = false)
    private String status = "OPEN"; // OPEN, PENDING, ASSIGNED, COMPLETED, CANCELLED

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private User merchant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_farmer_id")
    private User assignedFarmer;

    @OneToMany(mappedBy = "contract", cascade = CascadeType.ALL)
    private List<Bid> bids = new ArrayList<>();

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Double getQuantity() {
        return quantity;
    }

    public void setQuantity(Double quantity) {
        this.quantity = quantity;
    }

    public Double getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(Double basePrice) {
        this.basePrice = basePrice;
    }

    public String getQualityGrade() {
        return qualityGrade;
    }

    public void setQualityGrade(String qualityGrade) {
        this.qualityGrade = qualityGrade;
    }

    public LocalDateTime getBiddingEndDate() {
        return biddingEndDate;
    }

    public void setBiddingEndDate(LocalDateTime biddingEndDate) {
        this.biddingEndDate = biddingEndDate;
    }

    public LocalDateTime getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDateTime deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getMerchant() {
        return merchant;
    }

    public void setMerchant(User merchant) {
        this.merchant = merchant;
    }

    public User getAssignedFarmer() {
        return assignedFarmer;
    }

    public void setAssignedFarmer(User assignedFarmer) {
        this.assignedFarmer = assignedFarmer;
    }

    public List<Bid> getBids() {
        return bids;
    }

    public void setBids(List<Bid> bids) {
        this.bids = bids;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void addBid(Bid bid) {
        bids.add(bid);
        bid.setContract(this);
    }

    public void removeBid(Bid bid) {
        bids.remove(bid);
        bid.setContract(null);
    }
}
