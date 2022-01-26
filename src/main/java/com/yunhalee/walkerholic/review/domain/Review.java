package com.yunhalee.walkerholic.review.domain;

import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.review.exception.InvalidRatingException;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "review")
@Getter
@Setter
public class Review extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer id;

    @Column(nullable = false)
    private Integer rating;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Review(@NonNull Integer rating, String comment, @NonNull User user,
        @NonNull Product product) {
        checkRating(rating);
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.product = product;
    }

    public void update(Integer requestedRating, String requestedComment) {
        updateRating(requestedRating);
        this.comment = requestedComment;
    }

    private void updateRating(Integer requestedRating) {
        if (this.rating != requestedRating) {
            checkRating(requestedRating);
            product.editReview(this.rating, requestedRating);
            this.rating = requestedRating;
        }
    }

    private void checkRating(Integer rating) {
        if (rating < 0) {
            throw new InvalidRatingException("Rating should be greater than or equal to 0.");
        }
        if (5 < rating) {
            throw new InvalidRatingException("Rating should be lesser than or equal to 5.");
        }
    }
}
