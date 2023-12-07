package com.example.shopapi.services;

import com.example.shopapi.dto.OrderItemDTO;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import java.util.List;

public interface StripeService {
    Session createCheckoutSession(List<OrderItemDTO> items) throws StripeException;
    String getSessionStatus(String sessionId) throws StripeException;
}
