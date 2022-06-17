package com.yunhalee.walkerholic.cart.domain;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUserId(Integer userId);

    boolean existsByUserId(Integer userId);

    @Transactional
    void deleteByUserId(Integer userId);
}
