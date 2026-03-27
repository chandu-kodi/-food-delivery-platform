package com.example.deliveryservice.service;

import com.example.deliveryservice.dto.PaymentCompletedEvent;
import com.example.deliveryservice.entity.Delivery;
import com.example.deliveryservice.repository.DeliveryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Transactional
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository repository;

    public DeliveryServiceImpl(DeliveryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Delivery assignPartner(PaymentCompletedEvent event) {
        String partnerId = "partner-" + UUID.randomUUID();
        Delivery delivery = Delivery.builder()
                .orderId(event.getOrderId())
                .paymentId(event.getPaymentId())
                .partnerId(partnerId)
                .status("ASSIGNED")
                .assignedAt(LocalDateTime.now())
                .build();
        return repository.save(delivery);
    }

    @Override
    public Delivery getDeliveryById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found"));
    }

    @Override
    public Delivery getDeliveryByOrderId(Long orderId) {
        return repository.findByOrderId(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Delivery not found for order: " + orderId));
    }
}
