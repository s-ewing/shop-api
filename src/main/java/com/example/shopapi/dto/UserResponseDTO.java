package com.example.shopapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class UserResponseDTO {
    private String name;

    @Email
    @NotEmpty(message = "Email is required")
    private String email;

    private AddressDTO address;
    private List<OrderDTO> orders;
    private String jwt;
}
