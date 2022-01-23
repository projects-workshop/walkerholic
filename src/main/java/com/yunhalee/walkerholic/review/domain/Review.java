package com.yunhalee.walkerholic.review.domain;

import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.useractivity.domain.ActivityStatus;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    public Review(@NonNull Integer rating, String comment, @NonNull User user, @NonNull Product product){
        this.rating = rating;
        this.comment = comment;
        this.user = user;
        this.product = product;
    }

    public void update(Review requestedReview){
        updateRating(requestedReview.getRating());
        this.comment = requestedReview.getComment();
    }

    private void updateRating(Integer updatedRating){
        if (this.rating != updatedRating) {
            product.editReview(this.rating, updatedRating);
            this.rating = updatedRating;
        }
    }
}
