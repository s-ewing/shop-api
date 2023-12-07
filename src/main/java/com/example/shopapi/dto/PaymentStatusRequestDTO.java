package com.example.shopapi.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PaymentStatusRequestDTO {
    @NotEmpty(message = "Session id is required")
    @NotBlank(message = "Session id cannot be blank")
    private String sessionId;
}
