package com.example.shopapi.services.impl;

import com.example.shopapi.dto.OrderDTO;
import com.example.shopapi.enums.OrderStatus;
import com.example.shopapi.exceptions.ObjectNotFoundException;
import com.example.shopapi.mappers.OrderMapper;
import com.example.shopapi.models.Order;
import com.example.shopapi.models.OrderItem;
import com.example.shopapi.models.User;
import com.example.shopapi.repositories.OrderRepository;
import com.example.shopapi.repositories.UserRepository;
import com.example.shopapi.services.OrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }
    @Override
    public OrderDTO createOrder(OrderDTO orderDTO, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
        List<OrderItem> items = orderDTO.getItems().stream().map(OrderMapper::mapOrderItemDTOtoEntity).collect(Collectors.toList());
        Order order = new Order(items, OrderStatus.PENDING, LocalDateTime.now(), user);
        items.forEach(item -> item.setOrder(order));
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.mapEntityToOrderDTO(savedOrder);
    }

    @Override
    public OrderDTO updateOrder(OrderDTO orderDTO) {
        Long id = orderDTO.getId();
        Order existingOrder = orderRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException("order", id));
        existingOrder.setOrderStatus(orderDTO.getOrderStatus());
        Order updatedOrder = orderRepository.save(existingOrder);
        return OrderMapper.mapEntityToOrderDTO(updatedOrder);
    }
}
