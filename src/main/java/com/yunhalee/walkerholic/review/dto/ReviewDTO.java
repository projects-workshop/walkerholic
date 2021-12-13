package com.yunhalee.walkerholic.review.dto;

import com.yunhalee.walkerholic.review.domain.Review;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewDTO {

    private Integer id;

    private Integer rating;

    private String comment;

    private Integer userId;

    private String userFullname;

    private String userImageUrl;

    private Integer productId;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.rating = review.getRating();
        this.comment = review.getComment();
        this.userId = review.getUser().getId();
        this.userFullname = review.getUser().getFullname();
        this.userImageUrl = review.getUser().getImageUrl();
        this.productId = review.getProduct().getId();
        this.createdAt = review.getCreatedAt();
        this.updatedAt = review.getUpdatedAt();
    }
}
