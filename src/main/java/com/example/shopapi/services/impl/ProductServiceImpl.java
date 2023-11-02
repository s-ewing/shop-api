package com.example.shopapi.services.impl;

import com.example.shopapi.dto.ProductDTO;
import com.example.shopapi.mappers.ProductMapper;
import com.example.shopapi.models.Product;
import com.example.shopapi.repositories.ProductRepository;
import com.example.shopapi.services.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        Product product = productRepository.save(ProductMapper.mapProductDTOtoEntity(productDTO));
        return ProductMapper.mapEntityToProductDTO(product);
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream().map(ProductMapper::mapEntityToProductDTO).collect(Collectors.toList());
    }
}
