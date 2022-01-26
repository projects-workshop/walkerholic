package com.yunhalee.walkerholic.review.service;

import com.yunhalee.walkerholic.product.exception.ProductNotFoundException;
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
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    private ReviewRepository reviewRepository;
    private ProductRepository productRepository;
    private UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository,
        ProductRepository productRepository,
        UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }


    public ReviewResponse create(ReviewRequest reviewRequest) {
        User user = user(reviewRequest.getUserId());
        Product product = product(reviewRequest.getProductId());
        Review review = reviewRequest.toReview(user, product);
        product.addReview(review);
        reviewRepository.save(review);
        return new ReviewResponse(review);
    }

    public ReviewResponse update(ReviewRequest reviewRequest, Integer id) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new ReviewNotFoundException(
                "Review not found with id : " + id));
        review.update(reviewRequest.getRating(), reviewRequest.getComment());
        return new ReviewResponse(review);
    }

    private User user(Integer id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(
                "User not found with id : " + id));
    }

    private Product product(Integer id) {
        return productRepository.findById(id)
            .orElseThrow(() -> new ProductNotFoundException("Product not found with id : " + id));
    }

    public Integer deleteReview(Integer id) {
        Review review = reviewRepository.findById(id)
            .orElseThrow(() -> new ReviewNotFoundException(
                "Review not found with id : " + id));
        Product product = review.getProduct();
        product.deleteReview(review.getRating());
        reviewRepository.deleteById(id);
        return id;
    }

}
