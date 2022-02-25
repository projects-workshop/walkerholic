package com.yunhalee.walkerholic.review.service;

import com.yunhalee.walkerholic.product.exception.ProductNotFoundException;
import com.yunhalee.walkerholic.product.service.ProductService;
import com.yunhalee.walkerholic.review.dto.ReviewRequest;
import com.yunhalee.walkerholic.review.dto.ReviewResponse;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.review.domain.Review;
import com.yunhalee.walkerholic.review.exception.ReviewNotFoundException;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.product.domain.ProductRepository;
import com.yunhalee.walkerholic.review.domain.ReviewRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.user.exception.UserNotFoundException;
import com.yunhalee.walkerholic.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ReviewService {

    private ReviewRepository reviewRepository;
    private ProductService productService;
    private UserService userService;

    public ReviewService(ReviewRepository reviewRepository,
        ProductService productService,
        UserService userService) {
        this.reviewRepository = reviewRepository;
        this.productService = productService;
        this.userService = userService;
    }

    public ReviewResponse create(ReviewRequest reviewRequest) {
        User user = userService.findUserById(reviewRequest.getUserId());
        Product product = productService.findProductById(reviewRequest.getProductId());
        Review review = reviewRequest.toReview(user, product);
        product.addReview(review.getRating());
        reviewRepository.save(review);
        return new ReviewResponse(review);
    }

    public ReviewResponse update(ReviewRequest reviewRequest, Integer id) {
        Review review = findReviewById(id);
        review.update(reviewRequest.getRating(), reviewRequest.getComment());
        return new ReviewResponse(review);
    }

    public Integer deleteReview(Integer id) {
        Review review = findReviewById(id);
        Product product = review.getProduct();
        product.deleteReview(review.getRating());
        reviewRepository.deleteById(id);
        return id;
    }

    private Review findReviewById(Integer id) {
        return reviewRepository.findById(id)
            .orElseThrow(() -> new ReviewNotFoundException("Review not found with id : " + id));
    }

}
