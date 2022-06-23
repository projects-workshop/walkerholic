package com.yunhalee.walkerholic.cartItem.api;

import static com.yunhalee.walkerholic.cart.api.CartApiTest.FIRST_CART;
import static com.yunhalee.walkerholic.product.api.ProductApiTest.FIRST_PRODUCT;
import static com.yunhalee.walkerholic.product.api.ProductApiTest.SECOND_PRODUCT;
import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.FIRST_PRODUCT_IMAGE;
import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.SECOND_PRODUCT_IMAGE;
import static com.yunhalee.walkerholic.productImage.api.ProductImageApiTest.THIRD_PRODUCT_IMAGE;
import static com.yunhalee.walkerholic.review.api.ReviewApiTest.FIRST_REVIEW;
import static com.yunhalee.walkerholic.review.api.ReviewApiTest.SECOND_REVIEW;

import com.yunhalee.walkerholic.ApiTest;
import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.cartItem.domain.CartItem;
import com.yunhalee.walkerholic.product.domain.Product;
import lombok.NonNull;
import org.junit.jupiter.api.BeforeEach;

public class CartItemApiTest extends ApiTest {

    public static final CartItem FIRST_CART_ITEM = CartItem.builder()
        .id(1)
        .qty(2)
        .product(FIRST_PRODUCT)
        .cart(FIRST_CART).build();

    public static final CartItem SECOND_CART_ITEM = CartItem.builder()
        .id(1)
        .qty(2)
        .product(SECOND_PRODUCT)
        .cart(FIRST_CART).build();

    @BeforeEach
    void setUp() {
        FIRST_PRODUCT.addProductImage(FIRST_PRODUCT_IMAGE);
        FIRST_PRODUCT.addProductImage(SECOND_PRODUCT_IMAGE);
        SECOND_PRODUCT.addProductImage(THIRD_PRODUCT_IMAGE);
    }


}
