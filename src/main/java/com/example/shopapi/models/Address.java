package com.example.shopapi.models;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
@Embeddable
public class Address {
    @NotEmpty(message = "Street address is required")
    @NotBlank(message = "Street address cannot be blank")
    private String streetAddress;

    @NotEmpty(message = "City is required")
    @NotBlank(message = "City cannot be blank")
    private String city;

    @NotEmpty(message = "State is required")
    @NotBlank(message = "State cannot be blank")
    private String state;

    @NotEmpty(message = "Zip code is required")
    @NotBlank(message = "Zip code cannot be blank")
    private String zipCode;
}
