package com.yunhalee.walkerholic.review.domain;

import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.review.exception.InvalidRatingException;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "review")
@Getter
@NoArgsConstructor
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
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Builder
    public Review(Integer id, @NonNull Integer rating, String comment, @NonNull User user, @NonNull Product product) {
        checkRating(rating);
        this.id = id;
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.product = product;
    }

    public void update(Integer requestedRating, String requestedComment) {
        updateRating(requestedRating);
        this.comment = requestedComment;
    }

    public void changeProduct(Product product) {
        this.product = product;
    }

    private void updateRating(Integer requestedRating) {
        if (this.rating != requestedRating) {
            checkRating(requestedRating);
            this.product.editReview(this.rating, requestedRating);
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

    public Integer userId() {
        return this.user.getId();
    }

    public String userFullName() {
        return this.user.getFullName();
    }

    public String userImageUrl() {
        return this.user.getImageUrl();
    }

    public Integer productId() {
        return this.product.getId();
    }

}
