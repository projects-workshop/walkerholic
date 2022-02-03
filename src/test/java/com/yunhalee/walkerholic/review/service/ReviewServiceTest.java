package com.yunhalee.walkerholic.review.service;

import static org.junit.Assert.assertEquals;

import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.review.domain.Review;
import com.yunhalee.walkerholic.review.dto.ReviewRequest;
import com.yunhalee.walkerholic.review.dto.ReviewResponse;
import com.yunhalee.walkerholic.review.exception.InvalidRatingException;
import com.yunhalee.walkerholic.review.exception.ReviewNotFoundException;
import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional
class ReviewServiceTest extends MockBeans {

    @InjectMocks
    private ReviewService reviewService;

    private User user;
    private Review review;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User("testFirstName",
            "TestLastName",
            "test@example.com",
            "12345678",
            Role.USER);

        product = new Product("testProduct",
            "testBrand",
            Category.TUMBLER,
            12,
            1000f);

        review = Review.builder()
            .rating(2)
            .comment("testReview")
            .user(user)
            .product(product).build();
    }

    @ParameterizedTest
    @CsvSource({"2, test, 1, 2, 1", "1, testReview, 3, 2, 1", "5, testComment, 4, 3, 1"})
    @DisplayName("주어진 정보에 알맞은 리뷰를 생성한다.")
    void create_review(Integer rating, String comment, Integer productId, Integer userId,
        int expectedCount) {
        //given
        ReviewRequest request = new ReviewRequest(rating, comment, productId, userId);

        //when
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.of(user));
        when(productRepository.findById(anyInt())).thenReturn(java.util.Optional.of(product));
        ReviewResponse response = reviewService.create(request);

        //then
        assertEquals(response.getRating(), rating);
        assertEquals(response.getComment(), comment);
        assertEquals(product.getReviews().size(), expectedCount);
    }

    @ParameterizedTest
    @CsvSource({"6, test, 1, 2", "8, testReview, 3, 2", "-2, testComment, 4, 3"})
    @DisplayName("범위 밖의 점수로는 리뷰를 생성하지 않는다.")
    void create_review_with_rating_greater_than_5_or_lesser_than_0_is_invalid(Integer rating,
        String comment, Integer productId, Integer userId) {
        //given
        ReviewRequest request = new ReviewRequest(rating, comment, productId, userId);

        //when
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.of(user));
        when(productRepository.findById(anyInt())).thenReturn(java.util.Optional.of(product));
        assertThatThrownBy(() -> reviewService.create(request))
            .isInstanceOf(InvalidRatingException.class);
    }


    @ParameterizedTest
    @CsvSource({"4, test, 1, 4", "1, testReview, 4, 1", "5, testComment, 2, 5"})
    @DisplayName("주어진 정보로 리뷰를 업데이트 한다.")
    void update_review(Integer rating, String comment, Integer reviewId, Integer expected) {
        //given
        ReviewRequest request = new ReviewRequest(rating, comment);

        //when
        when(reviewRepository.findById(anyInt())).thenReturn(java.util.Optional.of(review));
        ReviewResponse response = reviewService.update(request, reviewId);

        //then
        assertEquals(response.getRating(), rating);
        assertEquals(response.getComment(), comment);
        assertEquals(review.getRating(), expected);
    }

    @ParameterizedTest
    @CsvSource({"6, test, 1", "8, testReview, 3", "-2, testComment, 4"})
    @DisplayName("범위 밖의 점수로는 리뷰를 수정하지 않는다.")
    void update_review_with_rating_greater_than_5_or_lesser_than_0_is_invalid(Integer rating,
        String comment, Integer reviewId) {
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
    @CsvSource({"4", "1", "5"})
    @DisplayName("리뷰를 삭제한다.")
    void delete_review(Integer reviewId) {
        //when
        when(reviewRepository.findById(anyInt())).thenReturn(java.util.Optional.of(review));
        reviewService.deleteReview(reviewId);

        //then
        verify(reviewRepository).deleteById(anyInt());
    }

    @ParameterizedTest
    @CsvSource({"23", "22", "44"})
    @DisplayName("존재하지 않는 아이디로는 리뷰를 삭제하지 않는다.")
    void delete_review_with_invalid_id(Integer reviewId) {
        assertThatThrownBy(() -> reviewService.deleteReview(reviewId))
            .isInstanceOf(ReviewNotFoundException.class);
    }


}