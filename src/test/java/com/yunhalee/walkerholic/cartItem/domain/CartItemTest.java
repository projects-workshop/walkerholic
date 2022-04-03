package com.yunhalee.walkerholic.cartItem.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.cart.domain.CartTest;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.domain.ProductTest;
import lombok.Builder;
import lombok.NonNull;

public class CartItemTest {

    public static final CartItem CART_ITEM = CartItem.builder()
        .id(1)
        .qty(2)
        .product(ProductTest.SECOND_PRODUCT).build();

}