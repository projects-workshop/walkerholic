package com.yunhalee.walkerholic.product.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.productImages i LEFT JOIN FETCH p.user u LEFT JOIN FETCH p.reviews r LEFT JOIN FETCH r.user s WHERE p.id=:id ")
    Product findByProductId(Integer id);

    @Query(value = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.productImages i LEFT JOIN FETCH p.user u LEFT JOIN FETCH p.reviews r LEFT JOIN FETCH r.user s WHERE u.id=:id",
        countQuery = "SELECT count(DISTINCT p) FROM Product p")
    Page<Product> findByUserId(Integer id, Pageable pageable);

    @Query(value = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.productImages i LEFT JOIN FETCH p.user u LEFT JOIN FETCH p.reviews r LEFT JOIN FETCH r.user s WHERE p.category=:category AND p.name LIKE %:keyword%",
        countQuery = "SELECT count(DISTINCT p) FROM Product p")
    Page<Product> findAllByCategory(Pageable pageable, Category category, String keyword);

    @Query(value = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.productImages i LEFT JOIN FETCH p.user u LEFT JOIN FETCH p.reviews r LEFT JOIN FETCH r.user s WHERE p.name LIKE %:keyword%",
        countQuery = "SELECT count(DISTINCT p) FROM Product p")
    Page<Product> findAllByKeyword(Pageable pageable, String keyword);

    @Query(value = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.productImages i LEFT JOIN FETCH p.user u LEFT JOIN FETCH p.reviews r LEFT JOIN FETCH r.user s WHERE u.id=:id AND p.category=:category AND p.name LIKE %:keyword% ",
        countQuery = "SELECT count(DISTINCT p) FROM Product p")
    Page<Product> findAllBySellerAndCategory(Pageable pageable, Integer id, Category category,
        String keyword);

    @Query(value = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.productImages i LEFT JOIN FETCH p.user u LEFT JOIN FETCH p.reviews r LEFT JOIN FETCH r.user s WHERE u.id=:id AND p.name LIKE %:keyword%",
        countQuery = "SELECT count(DISTINCT p) FROM Product p")
    Page<Product> findAllBySellerAndKeyword(Pageable pageable, Integer id, String keyword);

    @Query(value = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.productImages i LEFT JOIN FETCH p.user u ",
        countQuery = "SELECT count(DISTINCT p) FROM Product p")
    Page<Product> findAllProductList(Pageable pageable);
}
