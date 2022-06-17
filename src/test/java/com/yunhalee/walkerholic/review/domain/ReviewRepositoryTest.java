package com.yunhalee.walkerholic.review.domain;

import static org.assertj.core.api.Assertions.assertThat;

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

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ReviewRepositoryTest {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    private Review review;

    private User user;

    private Product product;

    @Before
    public void setUp() {
        user = User.builder()
            .firstname("firstName")
            .lastname("lastName")
            .email("testUser@example.com")
            .password("12345678")
            .role(Role.USER).build();
        userRepository.save(user);

        product = new Product("testProduct", "testBrand", Category.TUMBLER, 12, 1000f, "testDescription");
        productRepository.save(product);

        review = Review.builder()
            .rating(2)
            .comment("testReview")
            .user(user)
            .product(product).build();
    }


    @Test
    @DisplayName("새로운 리뷰를 저장한다.")
    public void save_review() {
        //given

        //when
        Review createdReview = reviewRepository.save(review);

        //then
        Assertions.assertAll(
            () -> assertThat(createdReview.getId()).isNotNull(),
            () -> assertThat(createdReview.getComment()).isEqualTo(review.getComment()),
            () -> assertThat(createdReview.getRating()).isEqualTo(review.getRating()),
            () -> assertThat(createdReview.getUser().getId()).isEqualTo(user.getId()),
            () -> assertThat(createdReview.getProduct().getId()).isEqualTo(product.getId())
        );
    }


    @Test
    @DisplayName("review Id를 이용하여 리뷰를 조회한다.")
    public void get_review_with_review_id() {
        //given
        reviewRepository.save(review);

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