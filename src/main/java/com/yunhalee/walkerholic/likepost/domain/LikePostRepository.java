package com.yunhalee.walkerholic.likepost.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikePostRepository extends JpaRepository<LikePost, Integer> {

}
