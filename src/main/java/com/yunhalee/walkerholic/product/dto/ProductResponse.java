package com.yunhalee.walkerholic.product.dto;

import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.review.domain.Review;
import com.yunhalee.walkerholic.review.dto.ReviewResponse;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.dto.SimpleUserResponse;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

//    @Getter
//    static class ProductImage {
//
//        private String name;
//        private String imageUrl;
//
//        static List<ProductImage> imageList(
//            List<com.yunhalee.walkerholic.product.domain.ProductImage> productImages) {
//            List<ProductImage> productImageList = new ArrayList<>();
//            productImages
//                .forEach(productImage -> productImageList.add(new ProductImage(productImage)));
//            return productImageList;
//        }
//
//        public ProductImage(com.yunhalee.walkerholic.product.domain.ProductImage productImage) {
//            this.name = productImage.getName();
//            this.imageUrl = productImage.getFilePath();
//        }
//    }
//
//    @Getter
//    static class ProductUser {
//
//        private Integer id;
//        private String fullname;
//        private String email;
//        private String imageUrl;
//        private String description;
//
//        public ProductUser(User user) {
//            this.id = user.getId();
//            this.fullname = user.getFullname();
//            this.email = user.getEmail();
//            this.imageUrl = user.getImageUrl();
//            this.description = user.getDescription();
//        }
//    }
//
//    @Getter
//    static class ProductReview {
//
//        private Integer id;
//        private Integer rating;
//        private String comment;
//        private Integer userId;
//        private String userFullname;
//        private String userImageUrl;
//
//        static List<ProductReview> reviewList(Set<Review> reviews) {
//            List<ProductReview> productReviews = new ArrayList<>();
//            reviews.forEach(review -> productReviews.add(new ProductReview(review)));
//            return productReviews;
//        }
//
//        public ProductReview(Review review) {
//            this.id = review.getId();
//            this.rating = review.getRating();
//            this.comment = review.getComment();
//            this.userId = review.getUser().getId();
//            this.userFullname = review.getUser().getFullname();
//            this.userImageUrl = review.getUser().getImageUrl();
//        }
//    }


}
