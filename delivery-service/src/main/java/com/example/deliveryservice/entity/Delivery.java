package com.example.deliveryservice.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;
    private Long paymentId;
    private String partnerId;
    private String status;
    private LocalDateTime assignedAt;

    // Default constructor
    public Delivery() {}

    // All args constructor
    public Delivery(Long id, Long orderId, Long paymentId, String partnerId, String status, LocalDateTime assignedAt) {
        this.id = id;
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.partnerId = partnerId;
        this.status = status;
        this.assignedAt = assignedAt;
    }

    // Builder pattern
    public static DeliveryBuilder builder() {
        return new DeliveryBuilder();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(Long paymentId) {
        this.paymentId = paymentId;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getAssignedAt() {
        return assignedAt;
    }

    public void setAssignedAt(LocalDateTime assignedAt) {
        this.assignedAt = assignedAt;
    }

    // Builder class
    public static class DeliveryBuilder {
        private Long id;
        private Long orderId;
        private Long paymentId;
        private String partnerId;
        private String status;
        private LocalDateTime assignedAt;

        public DeliveryBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DeliveryBuilder orderId(Long orderId) {
            this.orderId = orderId;
            return this;
        }

        public DeliveryBuilder paymentId(Long paymentId) {
            this.paymentId = paymentId;
            return this;
        }

        public DeliveryBuilder partnerId(String partnerId) {
            this.partnerId = partnerId;
            return this;
        }

        public DeliveryBuilder status(String status) {
            this.status = status;
            return this;
        }

        public DeliveryBuilder assignedAt(LocalDateTime assignedAt) {
            this.assignedAt = assignedAt;
            return this;
        }

        public Delivery build() {
            return new Delivery(id, orderId, paymentId, partnerId, status, assignedAt);
        }
    }
}
