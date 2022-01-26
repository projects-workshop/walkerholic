package com.yunhalee.walkerholic.review.service;

import static org.junit.Assert.assertEquals;

import com.yunhalee.walkerholic.product.domain.FakeProductRepository;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.domain.ProductRepository;
import com.yunhalee.walkerholic.review.domain.FakeReviewRepository;
import com.yunhalee.walkerholic.review.domain.Review;
import com.yunhalee.walkerholic.review.domain.ReviewRepository;
import com.yunhalee.walkerholic.review.dto.ReviewRequest;
import com.yunhalee.walkerholic.review.dto.ReviewResponse;
import com.yunhalee.walkerholic.user.domain.FakeUserRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class ReviewServiceTest {

    private ReviewRepository fakeReviewRepository = new FakeReviewRepository();

    private UserRepository fakeUserRepository = new FakeUserRepository();

    private ProductRepository fakeProductRepository = new FakeProductRepository();

    private ReviewService reviewService = new ReviewService(fakeReviewRepository,
        fakeProductRepository, fakeUserRepository);

    @ParameterizedTest
    @CsvSource({"2, test, 1, 2, 1", "1, testReview, 3, 2, 1", "5, testComment, 4, 3, 1"})
    @DisplayName("주어진 정보에 알맞은 리뷰를 생성한다.")
    void create_review(Integer rating, String comment, Integer productId, Integer userId,
        int expectedCount) {
        //given
        ReviewRequest request = new ReviewRequest(rating, comment, productId, userId);

//       //when
        ReviewResponse response = reviewService.create(request);
        Product product = fakeProductRepository.findById(productId).get();

        //then
        assertEquals(response.getRating(), rating);
        assertEquals(response.getComment(), comment);
        assertEquals(product.getReviews().size(), expectedCount);
    }

    @ParameterizedTest
    @CsvSource({"4, test, 1, 4", "1, testReview, 4, 1", "5, testComment, 2, 5"})
    @DisplayName("주어진 정보로 리뷰를 업데이트 한다.")
    void update_review(Integer rating, String comment, Integer reviewId, Float expected) {
        //given
        ReviewRequest request = new ReviewRequest(rating, comment);

//        //when
        ReviewResponse response = reviewService.update(request, reviewId);
        Review review = fakeReviewRepository.findById(1).get();

        //then
        assertEquals(response.getRating(), rating);
        assertEquals(response.getComment(), comment);
        assertEquals(review.getProduct().getAverage(), expected);
    }

    @ParameterizedTest
    @CsvSource({"4, 0", "1, 0", "5, 0"})
    @DisplayName("리뷰를 삭제한다.")
    void delete_review(Integer reviewId, Float expected) {
        //when
        reviewService.deleteReview(reviewId);
        Review review = fakeReviewRepository.findById(1).get();

        //then
        assertEquals(review.getProduct().getAverage(), expected);
    }


}