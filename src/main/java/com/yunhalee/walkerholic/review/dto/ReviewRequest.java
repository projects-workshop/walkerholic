package com.yunhalee.walkerholic.review.dto;

import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.review.domain.Review;
import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;

@Getter
public class ReviewRequest {

    private Integer rating;

    private String comment;

    private Integer productId;

    private Integer userId;

    public ReviewRequest() {
    }

    public ReviewRequest(Integer rating, String comment, Integer productId, Integer userId) {
        this.rating = rating;
        this.comment = comment;
        this.productId = productId;
        this.userId = userId;
    }


    public Review toReview(User user, Product product) {
        return Review.builder()
            .rating(rating)
            .comment(comment)
            .user(user)
            .product(product).build();
    }

}
