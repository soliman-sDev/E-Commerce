package com.e.ecommerce.service;

import com.e.ecommerce.dto.ProductRequest;
import com.e.ecommerce.dto.ProductResponse;
import com.e.ecommerce.entity.Product;
import com.e.ecommerce.repository.ProductRepo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    @Autowired
    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());

        Product p = productRepo.save(product);
        return toResponse(p);
    }

    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());
        product.setUpdatedAt(LocalDateTime.now());

        Product updated = productRepo.save(product);
        return toResponse(updated);
    }

    public void deleteProduct(Long id) {
        if (!productRepo.existsById(id)) {
            throw new EntityNotFoundException("Product not found");
        }
        productRepo.deleteById(id);
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return toResponse(product);
    }

    public Page<ProductResponse> getAllProducts(String category, Pageable pageable) {
         Page<Product> page = (category == null || category.isEmpty())
                ? productRepo.findAll(pageable)
                : productRepo.findAllByCategory(category, pageable);

        return page.map(this::toResponse);
    }

    private ProductResponse toResponse(Product product) {
        ProductResponse response = new ProductResponse();
        response.setId(product.getId());
        response.setName(product.getName());
        response.setDescription(product.getDescription());
        response.setPrice(product.getPrice());
        response.setStock(product.getStock());
        response.setCategory(product.getCategory());
        response.setImageUrl(product.getImageUrl());
        response.setCreatedAt(product.getCreatedAt());
        response.setUpdatedAt(product.getUpdatedAt());
        response.setMessage("Successfully");
        response.setStatusCode(200);
        return response;
    }
}
