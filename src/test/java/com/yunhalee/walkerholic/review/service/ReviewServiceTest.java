package com.yunhalee.walkerholic.review.service;

import com.yunhalee.walkerholic.ServiceTest;
import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.review.domain.Review;
import com.yunhalee.walkerholic.review.dto.ReviewRequest;
import com.yunhalee.walkerholic.review.dto.ReviewResponse;
import com.yunhalee.walkerholic.review.exception.InvalidRatingException;
import com.yunhalee.walkerholic.review.exception.ReviewNotFoundException;
import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ReviewServiceTest extends ServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    private User user;
    private Review review;
    private Product product;

    @BeforeEach
    void setUp() {
        user = User.builder()
            .firstname("firstName")
            .lastname("lastName")
            .email("testUser@example.com")
            .password("12345678")
            .role(Role.USER).build();

        product = new Product("testProduct",
            "testBrand",
            Category.TUMBLER,
            12,
            1000f,
            "testDescription");

        review = Review.builder()
            .rating(2)
            .comment("testReview")
            .user(user)
            .product(product).build();
    }

    @ParameterizedTest
    @CsvSource({"2, test, 1, 2, 1, 2.00", "1, testReview, 3, 2, 1, 1.00",
        "5, testComment, 4, 3, 1, 5.00"})
    @DisplayName("주어진 정보에 알맞은 리뷰를 생성한다.")
    void create_review(Integer rating, String comment, Integer productId, Integer userId, int expectedCount, BigDecimal expected) {
        //given
        ReviewRequest request = new ReviewRequest(rating, comment, productId, userId);

        //when
        when(userService.findUserById(anyInt())).thenReturn(user);
        when(productService.findProductById(anyInt())).thenReturn(product);
        ReviewResponse response = reviewService.create(request);

        //then
        assertThat(response.getRating()).isEqualTo(rating);
        assertThat(response.getComment()).isEqualTo(comment);
        assertThat(product.getReviewInfo().getTotalCount()).isEqualTo(expectedCount);
        assertThat(product.getAverage()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"6, test, 1, 2", "8, testReview, 3, 2", "-2, testComment, 4, 3"})
    @DisplayName("범위 밖의 점수로는 리뷰를 생성하지 않는다.")
    void create_review_with_rating_greater_than_5_or_lesser_than_0_is_invalid(Integer rating, String comment, Integer productId, Integer userId) {
        //given
        ReviewRequest request = new ReviewRequest(rating, comment, productId, userId);

        //when
        when(userService.findUserById(anyInt())).thenReturn(user);
        when(productService.findProductById(anyInt())).thenReturn(product);
        assertThatThrownBy(() -> reviewService.create(request))
            .isInstanceOf(InvalidRatingException.class);
    }


    @ParameterizedTest
    @CsvSource({"4, test, 1, 4.00", "1, testReview, 4, 1.00", "5, testComment, 2, 5.00"})
    @DisplayName("주어진 정보로 리뷰를 업데이트 한다.")
    void update_review(Integer rating, String comment, Integer reviewId, BigDecimal expected) {
        //given
        product.addReview(review.getRating());
        ReviewRequest request = new ReviewRequest(rating, comment);

        //when
        when(reviewRepository.findById(anyInt())).thenReturn(java.util.Optional.of(review));
        ReviewResponse response = reviewService.update(request, reviewId);

        //then
        assertThat(response.getRating()).isEqualTo(rating);
        assertThat(response.getComment()).isEqualTo(comment);
        assertThat(product.getAverage()).isEqualTo(expected);
    }

    @ParameterizedTest
    @CsvSource({"6, test, 1", "8, testReview, 3", "-2, testComment, 4"})
    @DisplayName("범위 밖의 점수로는 리뷰를 수정하지 않는다.")
    void update_review_with_rating_greater_than_5_or_lesser_than_0_is_invalid(Integer rating, String comment, Integer reviewId) {
        ReviewRequest request = new ReviewRequest(rating, comment);
        when(reviewRepository.findById(anyInt())).thenReturn(java.util.Optional.of(review));
        assertThatThrownBy(() -> reviewService.update(request, reviewId))
            .isInstanceOf(InvalidRatingException.class);
    }

    @ParameterizedTest
    @CsvSource({"6, test, 11", "8, testReview, 33", "-2, testComment, 44"})
    @DisplayName("존재하지 않는 아이디로는 리뷰를 수정하지 않는다.")
    void update_review_with_invalid_id(Integer rating, String comment, Integer reviewId) {
        ReviewRequest request = new ReviewRequest(rating, comment);
        assertThatThrownBy(() -> reviewService.update(request, reviewId))
            .isInstanceOf(ReviewNotFoundException.class);
    }

    @ParameterizedTest
    @CsvSource({"4, 0", "1, 0", "5, 0"})
    @DisplayName("리뷰를 삭제한다.")
    void delete_review(Integer reviewId, BigDecimal expected) {
        //when
        product.addReview(review.getRating());

        when(reviewRepository.findById(anyInt())).thenReturn(java.util.Optional.of(review));
        reviewService.deleteReview(reviewId);

        //then
        verify(reviewRepository).deleteById(anyInt());
        assertThat(product.getAverage()).isEqualTo(expected);
    }

    @ParameterizedTest
    @ValueSource(ints = {23, 22, 44})
    @DisplayName("존재하지 않는 아이디로는 리뷰를 삭제하지 않는다.")
    void delete_review_with_invalid_id(Integer reviewId) {
        assertThatThrownBy(() -> reviewService.deleteReview(reviewId))
            .isInstanceOf(ReviewNotFoundException.class);
    }


}