package com.yunhalee.walkerholic.user.domain;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    @Override
    @Query(value = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.userInfo LEFT JOIN FETCH u.userAuth LEFT JOIN FETCH u.userLevel WHERE u.id=?1")
    Optional<User> findById(Integer id);

    @Query(value = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.userInfo LEFT JOIN FETCH u.userAuth LEFT JOIN FETCH u.userLevel",
        countQuery = "SELECT count(DISTINCT u) FROM User u")
    Page<User> findAllUsers(Pageable pageable);

    @Query(value = "SELECT DISTINCT u FROM User u LEFT JOIN FETCH u.userInfo LEFT JOIN FETCH u.userAuth LEFT JOIN FETCH u.userLevel WHERE u.firstname LIKE %:keyword% OR u.lastname LIKE %:keyword%")
    List<User> findByKeyword(String keyword);

    boolean existsByEmail(String email);

}
