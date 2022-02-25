package com.yunhalee.walkerholic.product.service;

import com.yunhalee.walkerholic.product.dto.ProductImageResponse;
import com.yunhalee.walkerholic.product.dto.ProductRequest;
import com.yunhalee.walkerholic.product.dto.ProductResponse;
import com.yunhalee.walkerholic.product.dto.ProductResponses;
import com.yunhalee.walkerholic.product.dto.SimpleProductResponse;
import com.yunhalee.walkerholic.product.exception.ProductNotFoundException;
import com.yunhalee.walkerholic.review.domain.Review;
import com.yunhalee.walkerholic.review.domain.ReviewRepository;
import com.yunhalee.walkerholic.review.dto.ReviewResponse;
import com.yunhalee.walkerholic.user.dto.SimpleUserResponse;
import com.yunhalee.walkerholic.user.dto.SellerUserResponse;
import com.yunhalee.walkerholic.common.service.S3ImageUploader;
import com.yunhalee.walkerholic.user.service.UserService;
import com.yunhalee.walkerholic.util.FileUploadUtils;
import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.domain.ProductImage;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.product.domain.ProductImageRepository;
import com.yunhalee.walkerholic.product.domain.ProductRepository;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ProductService {

    public static final int PRODUCT_PER_PAGE = 9;
    public static final int PRODUCT_LIST_PER_PAGE = 10;

    private ProductRepository productRepository;
    private ReviewRepository reviewRepository;
    private UserService userService;
    private ProductImageRepository productImageRepository;
    private S3ImageUploader s3ImageUploader;
    private String bucketUrl;

    public ProductService(ProductRepository productRepository, ReviewRepository reviewRepository,
        UserService userService,
        ProductImageRepository productImageRepository,
        S3ImageUploader s3ImageUploader,
        @Value("${AWS_S3_BUCKET_URL}") String bucketUrl) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.productImageRepository = productImageRepository;
        this.s3ImageUploader = s3ImageUploader;
        this.bucketUrl = bucketUrl;
    }

    public SimpleProductResponse createProduct(ProductRequest request,
        List<MultipartFile> multipartFiles) {
        User user = userService.findUserById(request.getUserId());
        Product product = productRepository.save(request.toProduct(user));
        saveProductImage(product, multipartFiles);
        return SimpleProductResponse.of(product);
    }

    public SimpleProductResponse updateProduct(ProductRequest request, List<MultipartFile> multipartFiles, List<String> deletedImages) {
        Product product = findProductById(request.getId());
        product.update(request.toProduct());
        deleteProductImage(product, deletedImages);
        saveProductImage(product, multipartFiles);
        return SimpleProductResponse.of(product);
    }

    private void saveProductImage(Product product, List<MultipartFile> multipartFiles) {
        Optional.ofNullable(multipartFiles).orElseGet(Collections::emptyList)
            .forEach(multipartFile -> {
                try {
                    String uploadDir = "productUploads/" + product.getId();
                    String imageUrl = s3ImageUploader.uploadFile(uploadDir, multipartFile);
                    String fileName = imageUrl
                        .substring(bucketUrl.length() + uploadDir.length() + 2);
                    ProductImage productImage = productImageRepository
                        .save(ProductImage.of(fileName, imageUrl, product));
                    product.addProductImage(productImage);
                } catch (IOException ex) {
                    new IOException("Could not save file : " + multipartFile.getOriginalFilename());
                }
            });
    }

    private void deleteProductImage(Product product, List<String> deletedImages) {
        Optional.ofNullable(deletedImages).orElseGet(Collections::emptyList)
            .forEach(deletedImage -> {
                product.deleteProductImage(deletedImages);
                productImageRepository.deleteByFilePath(deletedImage);
                String fileName = deletedImage.substring(bucketUrl.length() + 1);
                s3ImageUploader.deleteFile(fileName);
            });
    }

    public ProductResponse getProduct(Integer id) {
        Product product = productRepository.findByProductId(id);
        List<Review> reviews = reviewRepository.findAllByPostId(id);
        return ProductResponse.of(product,
            productImageResponses(product.getProductImages()),
            SimpleUserResponse.of(product.getUser()),
            reviewResponses(reviews));
    }

    public ProductResponses getProducts(Integer page, String sort, String category, String keyword) {
        Pageable pageable = pageable(page, sort);
        if (category == null || category.isBlank()) {
            Page<Product> productPage = productRepository.findAllByKeyword(pageable, keyword);
            return productResponses(productPage);
        }
        Page<Product> productPage = productRepository.findAllByCategory(pageable, Category.valueOf(category), keyword);
        return productResponses(productPage);
    }

    public ProductResponses getProductsBySeller(Integer id, Integer page, String sort, String category, String keyword) {
        User seller = userService.findUserById(id);
        Pageable pageable = pageable(page, sort);
        if (category == null || category.isBlank()) {
            Page<Product> productPage = productRepository.findAllBySellerAndKeyword(pageable, id, keyword);
            return productResponses(productPage, seller);
        }
        Page<Product> productPage = productRepository.findAllBySellerAndCategory(pageable, id, Category.valueOf(category), keyword);
        return productResponses(productPage, seller);
    }

    public ProductResponses getAllProductList(Integer page, String sort) {
        Pageable pageable = PageRequest.of(page - 1, PRODUCT_LIST_PER_PAGE, Sort.by(sort));
        Page<Product> productPage = productRepository.findAllProductList(pageable);
        return productResponses(productPage);
    }

    public ProductResponses getProductListBySeller(Integer page, String sort, Integer id) {
        Pageable pageable = PageRequest.of(page - 1, PRODUCT_LIST_PER_PAGE, Sort.by(sort));
        Page<Product> productPage = productRepository.findByUserId(id, pageable);
        return productResponses(productPage);
    }

    private Pageable pageable(Integer page, String sort) {
        if (sort.equals("highest")) {
            return PageRequest.of(page - 1, PRODUCT_PER_PAGE, Sort.by(Sort.Direction.DESC, "price"));
        } else if (sort.equals("lowest")) {
            return PageRequest.of(page - 1, PRODUCT_PER_PAGE, Sort.by("price"));
        } else if (sort.equals("toprated")) {
            return PageRequest.of(page - 1, PRODUCT_PER_PAGE, Sort.by(Sort.Direction.DESC, "average"));
        }
        return PageRequest.of(page - 1, PRODUCT_PER_PAGE, Sort.by("createdAt"));
    }

    public void deleteProduct(Integer id) {
        String dir = "productUploads/" + id;
        s3ImageUploader.removeFolder(dir);
        productRepository.deleteById(id);
    }

    private List<ReviewResponse> reviewResponses(List<Review> reviews) {
        return reviews.stream()
            .map(ReviewResponse::new)
            .collect(Collectors.toList());
    }

    private List<ProductImageResponse> productImageResponses(List<ProductImage> productImages) {
        return productImages.stream()
            .map(ProductImageResponse::new)
            .collect(Collectors.toList());
    }

    private List<SimpleProductResponse> simpleProductResponses(List<Product> products) {
        return products.stream()
            .map(SimpleProductResponse::new)
            .collect(Collectors.toList());
    }

    private ProductResponses productResponses(Page<Product> productPage) {
        return ProductResponses.of(simpleProductResponses(productPage.getContent()),
            productPage.getTotalElements(),
            productPage.getTotalPages());
    }

    private ProductResponses productResponses(Page<Product> productPage, User seller) {
        return ProductResponses.of(simpleProductResponses(productPage.getContent()),
            SellerUserResponse.of(seller),
            productPage.getTotalElements(),
            productPage.getTotalPages());
    }

    public Product findProductById(Integer id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Product not found with id : " + id));
    }

}
