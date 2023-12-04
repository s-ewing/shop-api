package com.example.shopapi.services;

import com.example.shopapi.dto.ProductDTO;
import com.example.shopapi.enums.ProductCategory;
import com.example.shopapi.enums.ProductDepartment;
import com.example.shopapi.models.Product;
import com.example.shopapi.repositories.ProductRepository;
import com.example.shopapi.services.impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductServiceImpl productService;

    Product product1 = new Product();
    Product product2 = new Product();
    Product product3 = new Product();

    @BeforeEach
    void setUp() {
        product1.setId(1L);
        product1.setName("productName");
        product1.setDescription("productDescription");
        product1.setPrice(BigDecimal.valueOf(1));
        product1.setImgSrc("productImgSrc");
        product1.setCategories(new ArrayList<>(List.of(ProductCategory.SHOES)));
        product1.setDepartments(new ArrayList<>(List.of(ProductDepartment.MENS)));

        product2.setId(2L);
        product2.setName("productName");
        product2.setDescription("productDescription");
        product2.setPrice(BigDecimal.valueOf(2));
        product2.setImgSrc("productImgSrc");
        product2.setCategories(new ArrayList<>(List.of(ProductCategory.SHOES)));
        product2.setDepartments(new ArrayList<>(List.of(ProductDepartment.WOMENS)));

        product3.setId(3L);
        product3.setName("productName");
        product3.setDescription("productDescription");
        product3.setPrice(BigDecimal.valueOf(3));
        product3.setImgSrc("productImgSrc");
        product3.setCategories(new ArrayList<>(List.of(ProductCategory.HATS)));
    }

    @Test
    void getAllProducts() {
        List<Product> products = new ArrayList<>(List.of(product1, product2, product3));

        given(productRepository.findAll()).willReturn(products);

        List<ProductDTO> productDTOS = productService.getAllProducts();

        assertThat(productDTOS).hasSize(3);

        assertThat(productDTOS.get(0).getId()).isEqualTo(product1.getId());
        assertThat(productDTOS.get(1).getId()).isEqualTo(product2.getId());
        assertThat(productDTOS.get(2).getId()).isEqualTo(product3.getId());

        assertThat(productDTOS.get(0).getName()).isEqualTo(product1.getName());
        assertThat(productDTOS.get(1).getName()).isEqualTo(product2.getName());
        assertThat(productDTOS.get(2).getName()).isEqualTo(product3.getName());

        assertThat(productDTOS.get(0).getDescription()).isEqualTo(product1.getDescription());
        assertThat(productDTOS.get(1).getDescription()).isEqualTo(product2.getDescription());
        assertThat(productDTOS.get(2).getDescription()).isEqualTo(product3.getDescription());

        assertThat(productDTOS.get(0).getCategories()).isEqualTo(product1.getCategories());
        assertThat(productDTOS.get(1).getCategories()).isEqualTo(product2.getCategories());
        assertThat(productDTOS.get(2).getCategories()).isEqualTo(product3.getCategories());

        assertThat(productDTOS.get(0).getPrice()).isEqualTo(product1.getPrice());
        assertThat(productDTOS.get(1).getPrice()).isEqualTo(product2.getPrice());
        assertThat(productDTOS.get(2).getPrice()).isEqualTo(product3.getPrice());

        assertThat(productDTOS.get(0).getImgSrc()).isEqualTo(product1.getImgSrc());
        assertThat(productDTOS.get(1).getImgSrc()).isEqualTo(product2.getImgSrc());
        assertThat(productDTOS.get(2).getImgSrc()).isEqualTo(product3.getImgSrc());
    }
}