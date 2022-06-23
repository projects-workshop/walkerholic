package com.yunhalee.walkerholic.postImage.api;

import static com.yunhalee.walkerholic.product.api.ProductApiTest.FIRST_PRODUCT;
import static com.yunhalee.walkerholic.product.api.ProductApiTest.SECOND_PRODUCT;

import com.yunhalee.walkerholic.ApiTest;
import com.yunhalee.walkerholic.productImage.domain.ProductImage;

public class ProductImageApiTest extends ApiTest {

    public static final ProductImage FIRST_PRODUCT_IMAGE = ProductImage.builder()
        .id(1)
        .name("firstProductImage")
        .filePath("firstProductImage/image.png")
        .product(FIRST_PRODUCT).build();
    public static final ProductImage SECOND_PRODUCT_IMAGE = ProductImage.builder()
        .id(2)
        .name("secondProductImage")
        .filePath("secondProductImage/image.png")
        .product(FIRST_PRODUCT).build();
    public static final ProductImage THIRD_PRODUCT_IMAGE = ProductImage.builder()
        .id(3)
        .name("thirdProductImage")
        .filePath("thirdProductImage/image.png")
        .product(SECOND_PRODUCT).build();

}
