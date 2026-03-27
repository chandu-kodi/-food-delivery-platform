package com.example.paymentservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.paymentservice.entity.Payment;

public class PaymentResponse {
    private Long id;
    private Long orderId;
    private Long userId;
    private Long restaurantId;
    private String orderNumber;
    private Payment.PaymentMethod paymentMethod;
    private Payment.PaymentStatus status;
    private BigDecimal amount;
    private String transactionId;
    private String deliveryAddress;
    private String phoneNumber;
    private String specialInstructions;
    private Boolean paymentCollected;
    private LocalDateTime paymentCollectedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public PaymentResponse() {}

    // All-args constructor
    public PaymentResponse(Long id, Long orderId, Long userId, Long restaurantId, String orderNumber,
                          Payment.PaymentMethod paymentMethod, Payment.PaymentStatus status, BigDecimal amount,
                          String transactionId, String deliveryAddress, String phoneNumber,
                          String specialInstructions, Boolean paymentCollected,
                          LocalDateTime paymentCollectedAt, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.orderId = orderId;
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.orderNumber = orderNumber;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.amount = amount;
        this.transactionId = transactionId;
        this.deliveryAddress = deliveryAddress;
        this.phoneNumber = phoneNumber;
        this.specialInstructions = specialInstructions;
        this.paymentCollected = paymentCollected;
        this.paymentCollectedAt = paymentCollectedAt;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getOrderId() { return orderId; }
    public void setOrderId(Long orderId) { this.orderId = orderId; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(Long restaurantId) { this.restaurantId = restaurantId; }

    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }

    public Payment.PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(Payment.PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public Payment.PaymentStatus getStatus() { return status; }
    public void setStatus(Payment.PaymentStatus status) { this.status = status; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getSpecialInstructions() { return specialInstructions; }
    public void setSpecialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; }

    public Boolean getPaymentCollected() { return paymentCollected; }
    public void setPaymentCollected(Boolean paymentCollected) { this.paymentCollected = paymentCollected; }

    public LocalDateTime getPaymentCollectedAt() { return paymentCollectedAt; }
    public void setPaymentCollectedAt(LocalDateTime paymentCollectedAt) { this.paymentCollectedAt = paymentCollectedAt; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
