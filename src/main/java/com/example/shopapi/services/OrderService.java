package com.example.shopapi.services;

import com.example.shopapi.dto.OrderDTO;
import com.stripe.exception.StripeException;

public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDTO, Long userId) throws StripeException;
    OrderDTO updateOrderWithPaymentStatus(String sessionId) throws StripeException;
}
