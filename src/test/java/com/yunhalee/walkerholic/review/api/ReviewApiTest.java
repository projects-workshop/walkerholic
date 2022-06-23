package com.yunhalee.walkerholic.review.api;

import static com.yunhalee.walkerholic.product.api.ProductApiTest.FIRST_PRODUCT;
import static com.yunhalee.walkerholic.user.domain.UserTest.SECOND_USER;
import static com.yunhalee.walkerholic.user.domain.UserTest.THIRD_USER;

import com.yunhalee.walkerholic.ApiTest;
import com.yunhalee.walkerholic.review.domain.Review;

public class ReviewApiTest extends ApiTest {

    public static final Review FIRST_REVIEW = Review.builder()
        .id(1)
        .rating(4)
        .comment("firstTestReview")
        .user(SECOND_USER)
        .product(FIRST_PRODUCT).build();

    public static final Review SECOND_REVIEW = Review.builder()
        .id(2)
        .rating(3)
        .comment("secondTestReview")
        .user(THIRD_USER)
        .product(FIRST_PRODUCT).build();
}
