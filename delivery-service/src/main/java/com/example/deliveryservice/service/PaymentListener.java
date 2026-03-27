package com.example.deliveryservice.service;

import com.example.deliveryservice.dto.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentListener {

    private final DeliveryService deliveryService;

    @KafkaListener(topics = "payment-completed-topic", groupId = "${spring.kafka.consumer.group-id}", containerFactory = "kafkaListenerContainerFactory")
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        deliveryService.assignPartner(event);
        // order status update could be handled via another event or service call
    }
}
