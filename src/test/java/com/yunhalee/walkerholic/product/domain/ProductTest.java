package com.yunhalee.walkerholic.product.domain;

import com.yunhalee.walkerholic.productImage.domain.ProductImageTest;
import com.yunhalee.walkerholic.user.domain.UserTest;

public class ProductTest {

    public static final Product FIRST_PRODUCT = new Product("firstProduct",
        "testBrand",
        Category.TUMBLER,
        3,
        2.0f,
        "testDescription");
    public static final Product SECOND_PRODUCT = new Product(
        2,
        "secondProduct",
        "testDescription",
        "testBrand",
        Category.BAG,
        32,
        21.0f,
        UserTest.SELLER,
        ProductImageTest.PRODUCT_IMAGE);

    public static final Product THIRD_PRODUCT = new Product(
        "thirdProduct",
        "testBrand",
        Category.CLOTHES,
        5,
        28.0f,
        "testDescription");

}
