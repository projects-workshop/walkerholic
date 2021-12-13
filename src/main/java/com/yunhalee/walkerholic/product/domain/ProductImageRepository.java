package com.yunhalee.walkerholic.product.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface ProductImageRepository extends JpaRepository<ProductImage, Integer> {

    @Transactional
    Integer deleteByFilePath(String filePath);
}
