package com.example.shopapi.services;

import com.example.shopapi.dto.OrderDTO;

public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDTO, Long userId);
    OrderDTO updateOrder(OrderDTO orderDTO);
}
