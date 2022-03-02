package com.yunhalee.walkerholic.product.controller;

import com.yunhalee.walkerholic.product.dto.ProductRequest;
import com.yunhalee.walkerholic.product.dto.ProductResponse;
import com.yunhalee.walkerholic.product.dto.ProductResponses;
import com.yunhalee.walkerholic.product.dto.ProductSearchRequest;
import com.yunhalee.walkerholic.product.dto.SimpleProductResponse;
import com.yunhalee.walkerholic.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ResponseEntity<SimpleProductResponse> createProduct(
        @RequestPart("productRequest") ProductRequest productRequest,
        @RequestPart(value = "multipartFile") List<MultipartFile> multipartFiles) {
        return new ResponseEntity<>(productService.createProduct(productRequest, multipartFiles), HttpStatus.CREATED);
    }

    @PostMapping("/products/{id}")
    public ResponseEntity<SimpleProductResponse> updateProduct(@PathVariable("id") Integer id, @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.updateProduct(id, productRequest));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping("/products")
    public ResponseEntity<ProductResponses> getProducts(@RequestParam ProductSearchRequest request) {
        return ResponseEntity.ok(productService.getProducts(request));
    }

    @GetMapping("/users/{id}/products")
    public ResponseEntity<ProductResponses> getProductsBySeller(@PathVariable("id") Integer id, @RequestParam ProductSearchRequest request) {
        return ResponseEntity.ok(productService.getProductsBySeller(id, request));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


}
