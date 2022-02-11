package com.yunhalee.walkerholic.review.controller;


import com.yunhalee.walkerholic.review.dto.ReviewRequest;
import com.yunhalee.walkerholic.review.dto.ReviewResponse;
import com.yunhalee.walkerholic.review.service.ReviewService;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> create(@Valid @RequestBody ReviewRequest reviewRequest) {
        return ResponseEntity.ok(reviewService.create(reviewRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> update(@PathVariable("id") Integer id,
        @Valid @RequestBody ReviewRequest reviewRequest) {
        return ResponseEntity.ok(reviewService.update(reviewRequest, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> deleteReview(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(reviewService.deleteReview(id));
    }
}
