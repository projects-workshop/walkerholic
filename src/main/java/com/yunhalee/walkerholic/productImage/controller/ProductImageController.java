package com.yunhalee.walkerholic.productImage.controller;

import com.yunhalee.walkerholic.productImage.dto.SimpleProductImageResponses;
import com.yunhalee.walkerholic.productImage.service.ProductImageService;
import java.util.List;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/products/{id}/product-images")
public class ProductImageController {

    private final ProductImageService productImageService;

    public ProductImageController(ProductImageService productImageService) {
        this.productImageService = productImageService;
    }

    @PostMapping
    public ResponseEntity<SimpleProductImageResponses> createImages(@PathVariable("id") Integer id, @RequestParam(value = "multipartFile") List<MultipartFile> multipartFiles) {
        return ResponseEntity.ok(productImageService.createImages(id, multipartFiles));
    }

    @DeleteMapping
    public ResponseEntity deleteImages(@PathVariable("id") Integer id, @RequestParam(value = "deletedImages") List<String> deletedImages) {
        productImageService.deleteImages(id, deletedImages);
        return ResponseEntity.noContent().build();
    }
}
