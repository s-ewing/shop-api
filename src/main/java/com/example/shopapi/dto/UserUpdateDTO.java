package com.example.shopapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserUpdateDTO {
    private String name;
    private String password;
    @Email
    @NotEmpty(message = "Email is required")
    private String email;
    private AddressDTO address;
}
