package com.e.ecommerce.controller;

import com.e.ecommerce.dto.ProductRequest;
import com.e.ecommerce.dto.ProductResponse;
import com.e.ecommerce.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Products", description = "Manage products")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Create a new Product")
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.createProduct(request));

    }

    @Operation(summary = "Update Product")
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable Long id, @RequestBody ProductRequest request) {
        return ResponseEntity.ok(productService.updateProduct(id, request));
    }

    @Operation(summary = "Delete Product by Id")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
         productService.deleteProduct(id);
         return ResponseEntity.ok("Deleted Successfully");
    }

    @Operation(summary = "Get Product by Id")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @Operation(summary = "Get All Product")
    @GetMapping
    public ResponseEntity<Page<ProductResponse>> getAllProducts(@RequestParam(required = false) String category, Pageable pageable) {
        return  ResponseEntity.ok(productService.getAllProducts(category, pageable));
    }



}
