package com.example.shopapi.mappers;

import com.example.shopapi.dto.OrderDTO;
import com.example.shopapi.dto.OrderItemDTO;
import com.example.shopapi.enums.OrderStatus;
import com.example.shopapi.models.Order;
import com.example.shopapi.models.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {
    public static Order mapOrderDTOtoEntity(OrderDTO orderDTO) {
        Order order = new Order();
        List<OrderItem> items = orderDTO.getItems().stream().map(OrderMapper::mapOrderItemDTOtoEntity).collect(Collectors.toList());
        order.setItems(items);
        return order;
    }
    public static OrderDTO mapEntityToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        if (order.getItems() != null) {
            List<OrderItemDTO> items = order.getItems()
                    .stream()
                    .map(OrderMapper::mapEntityToOrderItemDTO)
                    .collect(Collectors.toList());
            orderDTO.setItems(items);
        }
        orderDTO.setId(order.getId());
        if (order.getOrderStatus() == OrderStatus.PENDING) {
            orderDTO.setOrderStatus(OrderStatus.CANCELED);
        } else {
            orderDTO.setOrderStatus(order.getOrderStatus());
        }
        orderDTO.setTimePlaced(order.getTimePlaced());
        orderDTO.setTotal(order.getTotal());
        orderDTO.setStripeClientSecret(order.getStripeClientSecret());
        return orderDTO;
    }

    public static OrderItem mapOrderItemDTOtoEntity(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(ProductMapper.mapProductDTOtoEntity(orderItemDTO.getProduct()));
        orderItem.setQuantity(orderItemDTO.getQuantity());
        return orderItem;
    }

    public static OrderItemDTO mapEntityToOrderItemDTO(OrderItem orderItem) {
        OrderItemDTO orderItemDTO = new OrderItemDTO();
        orderItemDTO.setProduct(ProductMapper.mapEntityToProductDTO(orderItem.getProduct()));
        orderItemDTO.setQuantity(orderItem.getQuantity());
        return orderItemDTO;
    }
}
