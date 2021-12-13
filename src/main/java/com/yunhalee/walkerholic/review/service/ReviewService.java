package com.yunhalee.walkerholic.review.service;

import com.yunhalee.walkerholic.review.dto.ReviewCreateDTO;
import com.yunhalee.walkerholic.review.dto.ReviewDTO;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.review.domain.Review;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.product.domain.ProductRepository;
import com.yunhalee.walkerholic.review.domain.ReviewRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewDTO saveReview(ReviewCreateDTO reviewCreateDTO) {
        if (reviewCreateDTO.getId() != null) {
            Review existingReview = reviewRepository.findByReviewId(reviewCreateDTO.getId());
            if (existingReview.getRating() != reviewCreateDTO.getRating()) {
                Product product = productRepository.findById(existingReview.getProduct().getId())
                    .get();
                product.editReview(existingReview.getRating(), reviewCreateDTO.getRating());
                productRepository.save(product);
                existingReview.setRating(reviewCreateDTO.getRating());
            }
            existingReview.setComment(reviewCreateDTO.getComment());
            reviewRepository.save(existingReview);
            return new ReviewDTO(existingReview);
        } else {
            User user = userRepository.findById(reviewCreateDTO.getUserId()).get();
            Product product = productRepository.findById(reviewCreateDTO.getProductId()).get();
            Review review = Review
                .createReview(reviewCreateDTO.getRating(), reviewCreateDTO.getComment(), user,
                    product);
            product.addReview(review);
            productRepository.save(product);
            reviewRepository.save(review);
            return new ReviewDTO(review);
        }
    }

    public Integer deleteReview(Integer id) {
        Review review = reviewRepository.findById(id).get();
        Product product = productRepository.findById(review.getProduct().getId()).get();
        product.deleteReview(review.getRating());
        reviewRepository.deleteById(id);
        return id;
    }
}
