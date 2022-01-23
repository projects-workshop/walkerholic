package com.yunhalee.walkerholic.review.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query(value = "SELECT DISTINCT r FROM Review r LEFT JOIN FETCH r.user u LEFT JOIN FETCH r.product p WHERE r.id=:id")
    Review findByReviewId(Integer id);
}
