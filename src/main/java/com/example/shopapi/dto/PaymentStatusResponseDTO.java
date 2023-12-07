package com.example.shopapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PaymentStatusResponseDTO {
    @NotNull
    OrderDTO orderDTO;

    @NotEmpty(message = "Token is required")
    @NotBlank(message = "Token cannot be blank")
    String token;

    public PaymentStatusResponseDTO(OrderDTO orderDTO, String token) {
        this.orderDTO = orderDTO;
        this.token = token;
    }
}
