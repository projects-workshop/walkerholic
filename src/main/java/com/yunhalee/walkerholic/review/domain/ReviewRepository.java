package com.yunhalee.walkerholic.review.domain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    @Query(value = "SELECT DISTINCT r FROM Review r LEFT JOIN FETCH r.user u LEFT JOIN FETCH u.userInfo LEFT JOIN FETCH r.product p WHERE r.id=:id")
    Review findByReviewId(Integer id);

    @Query(value = "SELECT DISTINCT r FROM Review r LEFT JOIN FETCH r.user u LEFT JOIN FETCH u.userInfo LEFT JOIN FETCH r.product p WHERE p.id=:id")
    List<Review> findAllByPostId(Integer id);
}
