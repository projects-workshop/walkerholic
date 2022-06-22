package com.yunhalee.walkerholic.review.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yunhalee.walkerholic.RepositoryTest;
import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.domain.ProductRepository;
import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

public class ReviewRepositoryTest extends RepositoryTest {

    private Review review;

    private User user;

    private Product product;

    @Before
    public void setUp() {
        user = userRepository.save(User.builder()
            .firstname("firstName")
            .lastname("lastName")
            .email("testUser@example.com")
            .password("12345678")
            .role(Role.USER).build());
        product = productRepository.save(new Product("testProduct",
            "testBrand",
            Category.TUMBLER,
            12,
            1000f,
            "testDescription"));
        review = reviewRepository.save(Review.builder()
            .rating(2)
            .comment("testReview")
            .user(user)
            .product(product).build());
    }


    @Test
    @DisplayName("review Id를 이용하여 리뷰를 조회한다.")
    public void get_review_with_review_id() {
        //when
        Review foundReview = reviewRepository.findByReviewId(review.getId());

        //then
        Assertions.assertAll(
            () -> assertThat(foundReview.getId()).isEqualTo(review.getId()),
            () -> assertThat(foundReview.getComment()).isEqualTo(review.getComment()),
            () -> assertThat(foundReview.getRating()).isEqualTo(review.getRating()),
            () -> assertThat(foundReview.getUser().getId()).isEqualTo(user.getId()),
            () -> assertThat(foundReview.getProduct().getId()).isEqualTo(product.getId())
        );
    }

}