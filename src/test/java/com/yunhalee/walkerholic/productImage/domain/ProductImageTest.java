package com.yunhalee.walkerholic.productImage.domain;

import com.yunhalee.walkerholic.productImage.domain.ProductImage;

public class ProductImageTest {

    public static final ProductImage PRODUCT_IMAGE = ProductImage.builder()
        .id(1)
        .name("testImageUrl")
        .filePath("https://walkerholic-test-you.s3.ap-northeast10.amazonaws.com/productUploads/testImageUrl").build();

    public static final ProductImage PRODUCT_SECOND_IMAGE = ProductImage.builder()
        .id(2)
        .name("secondImageUrl")
        .filePath("https://walkerholic-test-you.s3.ap-northeast10.amazonaws.com/productUploads/secondImageUrl").build();

}