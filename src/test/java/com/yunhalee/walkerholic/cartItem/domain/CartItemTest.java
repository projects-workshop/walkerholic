package com.yunhalee.walkerholic.cartItem.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.yunhalee.walkerholic.product.domain.ProductTest;

public class CartItemTest {

    public static final CartItem FIRST_CART_ITEM = CartItem.builder()
        .id(1)
        .qty(20)
        .product(ProductTest.SECOND_PRODUCT).build();

    public static final CartItem SECOND_CART_ITEM = CartItem.builder()
        .id(2)
        .qty(3)
        .product(ProductTest.THIRD_PRODUCT).build();

}