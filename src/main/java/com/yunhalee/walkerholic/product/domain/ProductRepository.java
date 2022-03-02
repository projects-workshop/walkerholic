package com.yunhalee.walkerholic.product.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query(value = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.productInfo LEFT JOIN FETCH p.productImages i LEFT JOIN FETCH i.productImages LEFT JOIN FETCH p.reviewInfo LEFT JOIN FETCH p.user u WHERE p.id=:id ")
    Product findByProductId(Integer id);

    @Query(value = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.productInfo d LEFT JOIN FETCH p.productImages i LEFT JOIN FETCH i.productImages LEFT JOIN FETCH p.reviewInfo WHERE d.category=:category AND d.name LIKE %:keyword%",
        countQuery = "SELECT count(DISTINCT p) FROM Product p")
    Page<Product> findAllByCategory(Pageable pageable, Category category, String keyword);

    @Query(value = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.productInfo d LEFT JOIN FETCH p.productImages i LEFT JOIN FETCH i.productImages LEFT JOIN FETCH p.reviewInfo WHERE d.name LIKE %:keyword%",
        countQuery = "SELECT count(DISTINCT p) FROM Product p")
    Page<Product> findAllByKeyword(Pageable pageable, String keyword);

    @Query(value = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.productInfo d LEFT JOIN FETCH p.productImages i LEFT JOIN FETCH i.productImages LEFT JOIN FETCH p.reviewInfo LEFT JOIN FETCH p.user u WHERE u.id=:id AND d.category=:category AND d.name LIKE %:keyword% ",
        countQuery = "SELECT count(DISTINCT p) FROM Product p")
    Page<Product> findAllBySellerAndCategory(Pageable pageable, Integer id, Category category,
        String keyword);

    @Query(value = "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.productInfo d LEFT JOIN FETCH p.productImages i LEFT JOIN FETCH i.productImages LEFT JOIN FETCH p.reviewInfo LEFT JOIN FETCH p.user u WHERE u.id=:id AND d.name LIKE %:keyword%",
        countQuery = "SELECT count(DISTINCT p) FROM Product p")
    Page<Product> findAllBySellerAndKeyword(Pageable pageable, Integer id, String keyword);

}
