package com.yunhalee.walkerholic.product.domain;

import static org.junit.jupiter.api.Assertions.*;

public class ProductImageTest {

    public static final ProductImage PRODUCT_IMAGE = new ProductImage(1,
        "testImageUrl",
        "https://walkerholic-test-you.s3.ap-northeast10.amazonaws.com/productUploads/testImageUrl");

    public static final ProductImage PRODUCT_SECOND_IMAGE = new ProductImage(2,
        "secondImageUrl",
        "https://walkerholic-test-you.s3.ap-northeast10.amazonaws.com/productUploads/secondImageUrl");

}