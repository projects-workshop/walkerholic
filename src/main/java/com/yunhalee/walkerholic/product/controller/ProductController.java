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

    @PatchMapping("/products")
    public ResponseEntity<SimpleProductResponse> updateProduct(
        @RequestPart("productRequest") ProductRequest productRequest,
        @RequestPart(value = "multipartFile", required = false) List<MultipartFile> multipartFiles,
        @RequestPart(value = "deletedImages", required = false) List<String> deletedImages) {
        return ResponseEntity.ok(productService.updateProduct(productRequest, multipartFiles, deletedImages));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(productService.getProduct(id));
    }

    @GetMapping("/products")
    public ResponseEntity<ProductResponses> getProducts(@RequestParam("page") Integer page,
        @RequestParam(value = "sort", required = false) String sort,
        @RequestParam(value = "category", required = false) String category,
        @RequestParam(value = "keyword", required = false) String keyword) {
        return ResponseEntity.ok(productService.getProducts(page, sort, category, keyword));
    }

    @GetMapping("/users/{id}/products")
    public ResponseEntity<ProductResponses> getProductsBySeller(@PathVariable("id") Integer id,
        @RequestParam("page") Integer page,
        @RequestParam(value = "sort", required = false) String sort,
        @RequestParam(value = "category", required = false) String category,
        @RequestParam(value = "keyword", required = false) String keyword) {
        return ResponseEntity.ok(productService.getProductsBySeller(id, page, sort, category, keyword));
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/products/list")
    public ResponseEntity<ProductResponses> getProductList(@RequestParam("page") Integer page,
        @RequestParam("sort") String sort) {
        return ResponseEntity.ok(productService.getAllProductList(page, sort));
    }

    @GetMapping("/users/{id}/products/list")
    public ResponseEntity<ProductResponses> getProductListBySeller(@RequestParam("page") Integer page,
        @RequestParam("sort") String sort, @PathVariable("id") Integer id) {
        return ResponseEntity.ok(productService.getProductListBySeller(page, sort, id));
    }


}
