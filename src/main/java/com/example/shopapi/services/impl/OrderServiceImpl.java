package com.example.shopapi.services.impl;

import com.example.shopapi.dto.OrderDTO;
import com.example.shopapi.enums.OrderStatus;
import com.example.shopapi.exceptions.ObjectNotFoundException;
import com.example.shopapi.exceptions.StripeSessionNotFoundException;
import com.example.shopapi.mappers.OrderMapper;
import com.example.shopapi.models.Order;
import com.example.shopapi.models.OrderItem;
import com.example.shopapi.models.User;
import com.example.shopapi.repositories.OrderRepository;
import com.example.shopapi.repositories.UserRepository;
import com.example.shopapi.services.OrderService;
import com.example.shopapi.services.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {
    private final StripeService stripeService;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    OrderServiceImpl(StripeService stripeService, OrderRepository orderRepository, UserRepository userRepository) {
        this.stripeService = stripeService;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }
    @Override
    public OrderDTO createOrder(OrderDTO orderDTO, Long userId) throws StripeException {
        User user = userRepository.findById(userId).orElseThrow(() -> new ObjectNotFoundException("user", userId));
        List<OrderItem> items = orderDTO.getItems().stream().map(OrderMapper::mapOrderItemDTOtoEntity).collect(Collectors.toList());
        Session session = stripeService.createCheckoutSession(orderDTO.getItems());
        Order order = new Order(items, OrderStatus.PENDING, LocalDateTime.now(), user, session.getId(), session.getClientSecret());
        items.forEach(item -> item.setOrder(order));
        Order savedOrder = orderRepository.save(order);
        return OrderMapper.mapEntityToOrderDTO(savedOrder);
    }

    @Override
    public OrderDTO updateOrderWithPaymentStatus(String sessionId) throws StripeException {
        Order existingOrder = orderRepository.findByStripeSessionId(sessionId).orElseThrow(() -> new StripeSessionNotFoundException("order", sessionId));
        Session session = Session.retrieve(sessionId);
        String status = session.getStatus();
        if (Objects.equals(status, "complete")) {
            existingOrder.setOrderStatus(OrderStatus.PAID);
        } else {
            existingOrder.setOrderStatus(OrderStatus.CANCELED);
        }
        Order updatedOrder = orderRepository.save(existingOrder);
        return OrderMapper.mapEntityToOrderDTO(updatedOrder);
    }
}
