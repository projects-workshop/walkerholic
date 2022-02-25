package com.yunhalee.walkerholic.product.controller;

import com.yunhalee.walkerholic.product.dto.ProductRequest;
import com.yunhalee.walkerholic.product.dto.ProductResponse;
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
        return new ResponseEntity<>(productService.createProduct(productRequest, multipartFiles),
            HttpStatus.CREATED);
    }

    @PatchMapping("/products")
    public ResponseEntity<SimpleProductResponse> updateProduct(
        @RequestPart("productRequest") ProductRequest productRequest,
        @RequestPart(value = "multipartFile", required = false) List<MultipartFile> multipartFiles,
        @RequestPart(value = "deletedImages", required = false) List<String> deletedImages) {
        return ResponseEntity.ok(productService.updateProduct(productRequest, multipartFiles, deletedImages));
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") Integer id) {
        return new ResponseEntity<ProductResponse>(productService.getProduct(id), HttpStatus.OK);
    }

    @GetMapping("/products")
    public ResponseEntity<?> getProducts(@RequestParam("page") Integer page,
        @RequestParam(value = "sort", required = false) String sort,
        @RequestParam(value = "category", required = false) String category,
        @RequestParam(value = "keyword", required = false) String keyword) {
        return new ResponseEntity<HashMap>(productService.getProducts(page, sort, category, keyword), HttpStatus.OK);
    }

    @GetMapping("/users/{id}/products")
    public ResponseEntity<?> getProductsBySeller(@PathVariable("id") Integer id,
        @RequestParam("page") Integer page,
        @RequestParam(value = "sort", required = false) String sort,
        @RequestParam(value = "category", required = false) String category,
        @RequestParam(value = "keyword", required = false) String keyword) {
        return new ResponseEntity<HashMap>(productService.getProductsBySeller(id, page, sort, category, keyword), HttpStatus.OK);
    }

    @DeleteMapping("/products/{id}")
    public Integer deleteProduct(@PathVariable("id") Integer id) {
        return productService.deleteProduct(id);
    }

    @GetMapping("/products/list")
    public ResponseEntity<?> getProductList(@RequestParam("page") Integer page,
        @RequestParam("sort") String sort) {
        return new ResponseEntity<HashMap<String, Object>>(productService.getAllProductList(page, sort), HttpStatus.OK);
    }

    @GetMapping("/users/{id}/products/list")
    public ResponseEntity<?> getProductListBySeller(@RequestParam("page") Integer page,
        @RequestParam("sort") String sort, @PathVariable("id") Integer id) {
        return new ResponseEntity<HashMap<String, Object>>(productService.getProductListBySeller(page, sort, id), HttpStatus.OK);
    }


}
