package com.yunhalee.walkerholic.order.domain;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    @Query(value = "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.user u LEFT JOIN FETCH o.orderItems i LEFT JOIN FETCH i.product p LEFT JOIN FETCH p.productImages m WHERE o.id=:id")
    Order findByOrderId(Integer id);

    @Query(value = "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderItems i LEFT JOIN FETCH i.product p LEFT JOIN FETCH p.productImages m LEFT JOIN FETCH o.user u WHERE u.id=:id AND o.orderStatus=:orderStatus")
    Optional<Order> findCartItemsByUserId(OrderStatus orderStatus, Integer id);

    @Query(value = "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderItems i LEFT JOIN FETCH i.product p LEFT JOIN FETCH p.user u LEFT JOIN FETCH o.user WHERE u.id=:id AND o.orderStatus!=:orderStatus ORDER BY o.createdAt",
        countQuery = "SELECT count(DISTINCT o) FROM Order o")
    Page<Order> findBySellerId(Pageable pageable, Integer id, OrderStatus orderStatus);

    @Query(value = "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderItems i LEFT JOIN FETCH i.product p LEFT JOIN FETCH p.user u LEFT JOIN FETCH o.user s WHERE s.id=:id AND o.orderStatus!=:orderStatus ORDER BY o.createdAt",
        countQuery = "SELECT count(DISTINCT o) FROM Order o")
    Page<Order> findByUserId(Pageable pageable, Integer id, OrderStatus orderStatus);

    @Query(value = "SELECT DISTINCT o FROM Order o LEFT JOIN FETCH o.orderItems i LEFT JOIN FETCH i.product p LEFT JOIN FETCH p.user u LEFT JOIN FETCH o.user WHERE o.orderStatus!=:orderStatus ORDER BY o.createdAt",
        countQuery = "SELECT count(DISTINCT o) FROM Order o")
    Page<Order> findAllOrders(Pageable pageable, OrderStatus orderStatus);

}
