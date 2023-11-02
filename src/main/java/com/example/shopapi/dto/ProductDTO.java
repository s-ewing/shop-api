package com.example.shopapi.dto;

import com.example.shopapi.enums.ProductCategory;
import com.example.shopapi.models.Product;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductDTO {
    private Long id;
    @NotEmpty(message = "Product name must contain at least 1 character")
    @NotBlank(message = "Product name cannot be blank")
    private String name;

    @NotEmpty(message = "Product description must contain at least 1 character")
    @NotBlank(message = "Product description may not be blank")
    private String description;

    @DecimalMin(value = "0.01", message = "Price must be a positive value")
    private BigDecimal price;

    @NotEmpty(message = "Product image source must contain at least 1 character")
    @NotBlank(message = "Product image source may not be blank")
    private String imgSrc;
    @NotEmpty(message = "Product must have one or more categories")
    private List<ProductCategory> categories;

    public ProductDTO(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.imgSrc = product.getImgSrc();
        this.categories = product.getCategories();
    }
}
