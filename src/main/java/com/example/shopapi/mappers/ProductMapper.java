package com.example.shopapi.mappers;

import com.example.shopapi.dto.ProductDTO;
import com.example.shopapi.models.Product;

public class ProductMapper {
    public static Product mapProductDTOtoEntity(ProductDTO productDTO) {
        Product product = new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setImgSrc(productDTO.getImgSrc());
        product.setPrice(productDTO.getPrice());
        product.setCategories(productDTO.getCategories());
        return product;
    }

    public static ProductDTO mapEntityToProductDTO(Product product) {
        ProductDTO productDTO= new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setImgSrc(product.getImgSrc());
        productDTO.setPrice(product.getPrice());
        productDTO.setCategories(product.getCategories());
        return productDTO;
    }
}
