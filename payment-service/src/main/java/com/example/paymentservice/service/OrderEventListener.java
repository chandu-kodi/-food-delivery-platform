package com.example.paymentservice.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.example.paymentservice.dto.OrderCreatedEvent;

@Component
public class OrderEventListener {

    private final PaymentService paymentService;

    // Explicit constructor
    public OrderEventListener(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @KafkaListener(topics = "order-created-topic", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void onOrderCreated(OrderCreatedEvent event) {
        paymentService.processPayment(event);
    }
}
