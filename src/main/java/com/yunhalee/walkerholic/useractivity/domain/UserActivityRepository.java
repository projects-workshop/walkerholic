package com.yunhalee.walkerholic.useractivity.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivityRepository extends JpaRepository<UserActivity, Integer> {

    @Query(value = "SELECT DISTINCT a FROM UserActivity a LEFT JOIN FETCH a.activity LEFT JOIN FETCH a.user u WHERE u.id=:id ORDER BY a.createdAt DESC",
        countQuery = "SELECT count(DISTINCT a) FROM UserActivity")
    Page<UserActivity> findByUserId(Pageable pageable, Integer id);

}
