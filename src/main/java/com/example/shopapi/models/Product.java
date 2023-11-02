package com.example.shopapi.models;

import com.example.shopapi.enums.ProductCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Product {
    @Id
    @SequenceGenerator(
            name = "product_sequence",
            sequenceName = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "product_sequence"
    )
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
    @ElementCollection
    @CollectionTable(name = "product_categories", joinColumns = @JoinColumn(name = "product_id"))
    @Enumerated(EnumType.STRING)
    private List<ProductCategory> categories;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Product product = (Product) o;
        return id != null && Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
