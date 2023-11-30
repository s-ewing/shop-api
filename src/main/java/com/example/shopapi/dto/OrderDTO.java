package com.example.shopapi.dto;

import com.example.shopapi.enums.OrderStatus;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
public class OrderDTO {
    private Long id;
    @NotEmpty(message = "Orders must contain at least 1 item")
    private List<OrderItemDTO> items;
    private OrderStatus orderStatus;
    private LocalDateTime timePlaced;
    private BigDecimal total;
}
