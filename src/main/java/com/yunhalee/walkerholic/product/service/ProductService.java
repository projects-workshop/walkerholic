package com.yunhalee.walkerholic.product.service;

import com.yunhalee.walkerholic.productImage.dto.ProductImageResponse;
import com.yunhalee.walkerholic.product.dto.ProductRequest;
import com.yunhalee.walkerholic.product.dto.ProductResponse;
import com.yunhalee.walkerholic.product.dto.ProductResponses;
import com.yunhalee.walkerholic.product.dto.SimpleProductResponse;
import com.yunhalee.walkerholic.product.exception.ProductNotFoundException;
import com.yunhalee.walkerholic.productImage.service.ProductImageService;
import com.yunhalee.walkerholic.review.domain.Review;
import com.yunhalee.walkerholic.review.domain.ReviewRepository;
import com.yunhalee.walkerholic.review.dto.ReviewResponse;
import com.yunhalee.walkerholic.user.dto.SimpleUserResponse;
import com.yunhalee.walkerholic.user.dto.SellerUserResponse;
import com.yunhalee.walkerholic.user.service.UserService;
import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.productImage.domain.ProductImage;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.product.domain.ProductRepository;
import java.util.stream.Collectors;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {

    public static final int PRODUCT_PER_PAGE = 9;
    public static final int PRODUCT_LIST_PER_PAGE = 10;

    private ProductRepository productRepository;
    private ReviewRepository reviewRepository;
    private UserService userService;
    private ProductImageService productImageService;

    public ProductService(ProductRepository productRepository, ReviewRepository reviewRepository, UserService userService, ProductImageService productImageService) {
        this.productRepository = productRepository;
        this.reviewRepository = reviewRepository;
        this.userService = userService;
        this.productImageService = productImageService;
    }

    @Transactional
    public SimpleProductResponse createProduct(ProductRequest request, List<MultipartFile> multipartFiles) {
        User user = userService.findUserById(request.getUserId());
        Product product = productRepository.save(request.toProduct(user));
        productImageService.uploadImages(product, multipartFiles);
        return SimpleProductResponse.of(product);
    }

    @Transactional
    public SimpleProductResponse updateProduct(Integer id, ProductRequest request) {
        Product product = findProductById(id);
        product.update(request.toProduct());
        return SimpleProductResponse.of(product);
    }

    public ProductResponse getProduct(Integer id) {
        Product product = productRepository.findByProductId(id);
        List<Review> reviews = reviewRepository.findAllByPostId(id);
        return ProductResponse.of(product,
            productImageResponses(product.getProductImages()),
            SimpleUserResponse.of(product.getUser()),
            reviewResponses(reviews));
    }

    @Cacheable(value = "products", key = "{#page, #sort, #category, #keyword}")
    public ProductResponses getProducts(Integer page, String sort, String category, String keyword) {
        Pageable pageable = pageable(page, sort);
        if (EnumUtils.isValidEnum(Category.class, category)) {
            Page<Product> productPage = productRepository.findAllByCategory(pageable, Category.valueOf(category), keyword);
            return productResponses(productPage);
        }
        Page<Product> productPage = productRepository.findAllByKeyword(pageable, keyword);
        return productResponses(productPage);
    }

    public ProductResponses getProductsBySeller(Integer id, Integer page, String sort,
        String category, String keyword) {
        User seller = userService.findUserById(id);
        Pageable pageable = pageable(page, sort);
        if (EnumUtils.isValidEnum(Category.class, category)) {
            Page<Product> productPage = productRepository.findAllBySellerAndCategory(pageable, id, Category.valueOf(category), keyword);
            return productResponses(productPage, seller);
        }
        Page<Product> productPage = productRepository.findAllBySellerAndKeyword(pageable, id, keyword);
        return productResponses(productPage, seller);
    }

    private Pageable pageable(Integer page, String sort) {
        if (sort.equals("highest")) {
            return PageRequest.of(page - 1, PRODUCT_PER_PAGE, Sort.by(Sort.Direction.DESC, "productInfo.price"));
        } else if (sort.equals("lowest")) {
            return PageRequest.of(page - 1, PRODUCT_PER_PAGE, Sort.by("productInfo.price"));
        } else if (sort.equals("toprated")) {
            return PageRequest.of(page - 1, PRODUCT_PER_PAGE, Sort.by(Sort.Direction.DESC, "reviewInfo.average"));
        }
        return PageRequest.of(page - 1, PRODUCT_PER_PAGE, Sort.by(sort));
    }

    @Transactional
    public void deleteProduct(Integer id) {
        productImageService.deleteProduct(id);
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
