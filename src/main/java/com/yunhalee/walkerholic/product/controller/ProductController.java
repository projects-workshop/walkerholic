package com.yunhalee.walkerholic.product.controller;

import com.yunhalee.walkerholic.product.dto.ProductCreateDTO;
import com.yunhalee.walkerholic.product.dto.ProductDTO;
import com.yunhalee.walkerholic.product.dto.ProductListDTO;
import com.yunhalee.walkerholic.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping("/product/save")
    public ProductListDTO saveProduct(@RequestParam(value = "id", required = false) Integer id,
        @RequestParam("name") String name,
        @RequestParam("description") String description,
        @RequestParam("brand") String brand,
        @RequestParam("category") String category,
        @RequestParam("stock") Integer stock,
        @RequestParam("price") Float price,
        @RequestParam("userId") Integer userId,
        @RequestParam(value = "multipartFile", required = false) List<MultipartFile> multipartFiles,
        @RequestParam(value = "deletedImages", required = false) List<String> deletedImages) {
        ProductCreateDTO productCreateDTO = new ProductCreateDTO(id, name, description, brand,
            category, stock, price, userId);
        return productService.saveProduct(productCreateDTO, multipartFiles, deletedImages);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") String id) {
        Integer productId = Integer.parseInt(id);
        return new ResponseEntity<ProductDTO>(productService.getProduct(productId), HttpStatus.OK);
    }

    @GetMapping("/products/{page}")
    public ResponseEntity<?> getProducts(@PathVariable("page") String page,
        @RequestParam(value = "sort", required = false) String sort,
        @RequestParam(value = "category", required = false) String category,
        @RequestParam(value = "keyword", required = false) String keyword) {
        Integer pageNumber = Integer.parseInt(page);
        return new ResponseEntity<HashMap>(
            productService.getProducts(pageNumber, sort, category, keyword), HttpStatus.OK);
    }

    @GetMapping("/products/seller/{id}/{page}")
    public ResponseEntity<?> getProductsBySeller(@PathVariable("id") String id,
        @PathVariable("page") String page,
        @RequestParam(value = "sort", required = false) String sort,
        @RequestParam(value = "category", required = false) String category,
        @RequestParam(value = "keyword", required = false) String keyword) {
        Integer sellerId = Integer.parseInt(id);
        Integer pageNumber = Integer.parseInt(page);
        return new ResponseEntity<HashMap>(
            productService.getProductsBySeller(sellerId, pageNumber, sort, category, keyword),
            HttpStatus.OK);
    }

    @DeleteMapping("/product/delete/{id}")
    public Integer deleteProduct(@PathVariable("id") String id) {
        Integer productId = Integer.parseInt(id);
        return productService.deleteProduct(productId);
    }

    @GetMapping("/productlist/{page}/{sort}")
    public ResponseEntity<?> getProductList(@PathVariable("page") String page,
        @PathVariable("sort") String sort) {
        Integer pageNumber = Integer.parseInt(page);
        return new ResponseEntity<HashMap<String, Object>>(
            productService.getAllProductList(pageNumber, sort), HttpStatus.OK);
    }

    @GetMapping("/productlistBySeller/{page}/{sort}/{id}")
    public ResponseEntity<?> getProductListBySeller(@PathVariable("page") String page,
        @PathVariable("sort") String sort, @PathVariable("id") String id) {
        Integer pageNumber = Integer.parseInt(page);
        Integer sellerId = Integer.parseInt(id);
        return new ResponseEntity<HashMap<String, Object>>(
            productService.getProductListBySeller(pageNumber, sort, sellerId), HttpStatus.OK);
    }


}
