package com.yunhalee.walkerholic.product.controller;

import com.yunhalee.walkerholic.product.dto.ProductRequest;
import com.yunhalee.walkerholic.product.dto.ProductResponse;
import com.yunhalee.walkerholic.product.dto.ProductResponses;
import com.yunhalee.walkerholic.product.dto.SimpleProductResponse;
import com.yunhalee.walkerholic.product.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/products")
    public ResponseEntity<SimpleProductResponse> createProduct(@RequestPart(value = "productRequest") ProductRequest productRequest, @RequestPart(value = "multipartFile") List<MultipartFile> multipartFiles) {
        return new ResponseEntity<>(productService.createProduct(productRequest, multipartFiles), HttpStatus.CREATED);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<SimpleProductResponse> updateProduct(@PathVariable("id") Integer id, @RequestBody ProductRequest productRequest) {
        return ResponseEntity.ok(productService.updateProduct(id, productRequest));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping("/products")
    public ResponseEntity<ProductResponses> getProducts(@RequestParam("page") Integer page, @RequestParam("sort") String sort, @RequestParam("category") String category, @RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(productService.getProducts(page, sort, category, keyword));
    }


    @GetMapping("/users/{id}/products")
    public ResponseEntity<ProductResponses> getProductsBySeller(@PathVariable("id") Integer id, @RequestParam("page") Integer page, @RequestParam("sort") String sort, @RequestParam("category") String category, @RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(productService.getProductsBySeller(id, page, sort, category, keyword));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }


}
