package com.example.paymentservice.service;

import com.example.paymentservice.dto.OrderCreatedEvent;
import com.example.paymentservice.dto.PaymentCompletedEvent;

public interface PaymentService {
    PaymentCompletedEvent processPayment(OrderCreatedEvent orderEvent);
    // additional common operations
    com.example.paymentservice.dto.PaymentResponse createPayment(com.example.paymentservice.dto.PaymentRequest request);
    com.example.paymentservice.dto.PaymentResponse getPaymentById(Long id);
}
