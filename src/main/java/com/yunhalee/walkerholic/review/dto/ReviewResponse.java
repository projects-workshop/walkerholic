package com.yunhalee.walkerholic.review.dto;

import com.yunhalee.walkerholic.review.domain.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponse {

    private Integer id;

    private Integer rating;

    private String comment;

    private Integer userId;

    private String userFullname;

    private String userImageUrl;

    private Integer productId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public ReviewResponse(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.userId = review.userId();
        this.userFullname = review.userFullName();
        this.userImageUrl = review.userImageUrl();
        this.productId = review.productId();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}
