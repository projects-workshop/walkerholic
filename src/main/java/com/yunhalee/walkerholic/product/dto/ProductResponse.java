package com.yunhalee.walkerholic.product.dto;

import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.productImage.dto.ProductImageResponse;
import com.yunhalee.walkerholic.review.dto.ReviewResponse;
import com.yunhalee.walkerholic.user.dto.SimpleUserResponse;
import java.math.BigDecimal;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductResponse {

    private Integer id;

    private String name;

    private String description;

    private String brand;

    private String category;

    private Integer stock;

    private Float price;

    private BigDecimal average;

    private List<ProductImageResponse> productImages;

    private SimpleUserResponse user;

    private List<ReviewResponse> productReviews;

    public ProductResponse() {
    }

    public ProductResponse(Product product, List<ProductImageResponse> productImages, SimpleUserResponse user, List<ReviewResponse> reviews) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.brand = product.getBrand();
        this.category = product.getCategory().name();
        this.stock = product.getStock();
        this.price = product.getPrice();
        this.productImages = productImages;
        this.user = user;
        this.productReviews = reviews;
        this.average = product.getAverage();
    }

    public static ProductResponse of(Product product, List<ProductImageResponse> productImages, SimpleUserResponse user, List<ReviewResponse> reviews){
        return new ProductResponse(product, productImages, user, reviews);
    }

}
