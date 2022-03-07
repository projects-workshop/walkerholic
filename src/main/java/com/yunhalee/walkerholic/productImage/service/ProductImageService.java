package com.yunhalee.walkerholic.productImage.service;

import com.yunhalee.walkerholic.common.service.S3ImageUploader;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.domain.ProductRepository;
import com.yunhalee.walkerholic.product.exception.ProductNotFoundException;
import com.yunhalee.walkerholic.productImage.domain.ProductImage;
import com.yunhalee.walkerholic.productImage.domain.ProductImageRepository;
import com.yunhalee.walkerholic.productImage.dto.SimpleProductImageResponses;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class ProductImageService {

    private ProductImageRepository productImageRepository;
    private ProductRepository productRepository;
    private S3ImageUploader s3ImageUploader;

    public ProductImageService(
        ProductImageRepository productImageRepository,
        ProductRepository productRepository,
        S3ImageUploader s3ImageUploader) {
        this.productImageRepository = productImageRepository;
        this.productRepository = productRepository;
        this.s3ImageUploader = s3ImageUploader;
    }

    public SimpleProductImageResponses createImages(Integer id,
        List<MultipartFile> multipartFiles) {
        Product product = findProductById(id);
        return SimpleProductImageResponses.of(uploadImages(product, multipartFiles));
    }

    public List<String> uploadImages(Product product, List<MultipartFile> multipartFiles) {
        List<String> imageUrls = new ArrayList<>();
        multipartFiles.forEach(multipartFile -> {
            ProductImage productImage = s3ImageUploader.uploadProductImage(product, multipartFile);
            productImageRepository.save(productImage);
            product.addProductImage(productImage);
            imageUrls.add(productImage.getFilePath());
        });
        return imageUrls;
    }

    public void deleteImages(Integer id, List<String> deletedImages) {
        Product product = findProductById(id);
        removeImages(product, deletedImages);
    }

    public void removeImages(Product product, List<String> deletedImages) {
        deletedImages.forEach(deletedImage -> {
            product.deleteProductImage(deletedImages);
            productImageRepository.deleteByFilePath(deletedImage);
            s3ImageUploader.deleteByFilePath(deletedImage);
        });
    }

    private Product findProductById(Integer id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Product not found with id : " + id));
    }
}
