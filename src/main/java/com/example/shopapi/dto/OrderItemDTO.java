package com.example.shopapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemDTO {
    @NotNull
    ProductDTO product;
    @Min(value = 1)
    Integer quantity;
}
