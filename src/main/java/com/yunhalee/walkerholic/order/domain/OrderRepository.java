package com.yunhalee.walkerholic.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.delivery LEFT JOIN FETCH o.payment LEFT JOIN FETCH o.orderItems r LEFT JOIN FETCH r.orderItems i LEFT JOIN FETCH i.product p LEFT JOIN FETCH p.productImages m WHERE o.id=:id")
    Order findByOrderId(Integer id);

    @Query(value = "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.delivery LEFT JOIN FETCH o.payment LEFT JOIN FETCH o.orderItems r LEFT JOIN FETCH r.orderItems i LEFT JOIN FETCH i.product p LEFT JOIN FETCH p.user u WHERE u.id=:id ORDER BY o.createdAt",
        countQuery = "SELECT count(DISTINCT o) FROM Order o")
    Page<Order> findBySellerId(Pageable pageable, Integer id);

    @Query(value = "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.delivery LEFT JOIN FETCH o.payment LEFT JOIN FETCH o.orderItems r LEFT JOIN FETCH r.orderItems i LEFT JOIN FETCH i.product p LEFT JOIN FETCH p.user u WHERE o.userId=:id ORDER BY o.createdAt",
        countQuery = "SELECT count(DISTINCT o) FROM Order o")
    Page<Order> findByUserId(Pageable pageable, Integer id);

    @Query(value = "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.delivery LEFT JOIN FETCH o.payment LEFT JOIN FETCH o.orderItems r LEFT JOIN FETCH r.orderItems i LEFT JOIN FETCH i.product p LEFT JOIN FETCH p.user u ORDER BY o.createdAt",
        countQuery = "SELECT count(DISTINCT o) FROM Order o")
    Page<Order> findAllOrders(Pageable pageable);

    boolean existsByCreatedAtBetweenAndUserId(LocalDateTime from, LocalDateTime to, Integer userId);

}
