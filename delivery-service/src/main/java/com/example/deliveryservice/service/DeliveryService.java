package com.example.deliveryservice.service;

import com.example.deliveryservice.dto.PaymentCompletedEvent;
import com.example.deliveryservice.entity.Delivery;

public interface DeliveryService {
    Delivery assignPartner(PaymentCompletedEvent event);
    Delivery getDeliveryById(Long id);
    Delivery getDeliveryByOrderId(Long orderId);
}
