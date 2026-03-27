package com.example.paymentservice.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "restaurant_id", nullable = false)
    private Long restaurantId;

    @Column(name = "order_number", nullable = false)
    private String orderNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(name = "amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_id")
    private String transactionId;

    @Column(name = "delivery_address", nullable = false, columnDefinition = "TEXT")
    private String deliveryAddress;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "special_instructions", columnDefinition = "TEXT")
    private String specialInstructions;

    @Column(name = "payment_collected")
    private Boolean paymentCollected = false;

    @Column(name = "payment_collected_at")
    private LocalDateTime paymentCollectedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = PaymentStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum PaymentMethod {
        CASH_ON_DELIVERY,
        CREDIT_CARD,
        DEBIT_CARD,
        UPI,
        WALLET
    }

    public enum PaymentStatus {
        PENDING,
        PROCESSING,
        COMPLETED,
        FAILED,
        REFUNDED,
        CANCELLED
    }
    
    // Default constructor
    public Payment() {}
    
    // All-args constructor
    public Payment(Long id, Long orderId, Long userId, Long restaurantId, String orderNumber,
                   PaymentMethod paymentMethod, PaymentStatus status, BigDecimal amount,
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
    
    // Builder pattern
    public static PaymentBuilder builder() {
        return new PaymentBuilder();
    }
    
    public static class PaymentBuilder {
        private Long id;
        private Long orderId;
        private Long userId;
        private Long restaurantId;
        private String orderNumber;
        private PaymentMethod paymentMethod;
        private PaymentStatus status;
        private BigDecimal amount;
        private String transactionId;
        private String deliveryAddress;
        private String phoneNumber;
        private String specialInstructions;
        private Boolean paymentCollected = false;
        private LocalDateTime paymentCollectedAt;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        
        public PaymentBuilder id(Long id) { this.id = id; return this; }
        public PaymentBuilder orderId(Long orderId) { this.orderId = orderId; return this; }
        public PaymentBuilder userId(Long userId) { this.userId = userId; return this; }
        public PaymentBuilder restaurantId(Long restaurantId) { this.restaurantId = restaurantId; return this; }
        public PaymentBuilder orderNumber(String orderNumber) { this.orderNumber = orderNumber; return this; }
        public PaymentBuilder paymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; return this; }
        public PaymentBuilder status(PaymentStatus status) { this.status = status; return this; }
        public PaymentBuilder amount(BigDecimal amount) { this.amount = amount; return this; }
        public PaymentBuilder transactionId(String transactionId) { this.transactionId = transactionId; return this; }
        public PaymentBuilder deliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; return this; }
        public PaymentBuilder phoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; return this; }
        public PaymentBuilder specialInstructions(String specialInstructions) { this.specialInstructions = specialInstructions; return this; }
        public PaymentBuilder paymentCollected(Boolean paymentCollected) { this.paymentCollected = paymentCollected; return this; }
        public PaymentBuilder paymentCollectedAt(LocalDateTime paymentCollectedAt) { this.paymentCollectedAt = paymentCollectedAt; return this; }
        public PaymentBuilder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public PaymentBuilder updatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; return this; }
        
        public Payment build() {
            return new Payment(id, orderId, userId, restaurantId, orderNumber, paymentMethod,
                             status, amount, transactionId, deliveryAddress, phoneNumber,
                             specialInstructions, paymentCollected, paymentCollectedAt, createdAt, updatedAt);
        }
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
    
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public PaymentStatus getStatus() { return status; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    
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
