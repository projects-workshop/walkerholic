package com.yunhalee.walkerholic.cartItem.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

import com.yunhalee.walkerholic.cart.domain.CartTest;
import com.yunhalee.walkerholic.product.domain.ProductTest;
import com.yunhalee.walkerholic.product.exception.NotEnoughStockException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CartItemTest {

    private static final String NOT_ENOUGH_STOCK_EXCEPTION = "Stock is not enough.";

    public static final CartItem FIRST_CART_ITEM = CartItem.builder()
        .id(1)
        .qty(20)
        .product(ProductTest.SECOND_PRODUCT).build();

    public static final CartItem SECOND_CART_ITEM = CartItem.builder()
        .id(2)
        .qty(3)
        .product(ProductTest.THIRD_PRODUCT).build();

    private CartItem cartItem;

    void setUp() {
        cartItem = CartItem.of(10, ProductTest.SECOND_PRODUCT, CartTest.CART);
    }

    @Test
    public void update_quantity() {
        // given
        setUp();
        Integer qty = 13;

        // when
        cartItem.changeQty(qty);

        // then
        assertThat(cartItem.getQty()).isEqualTo(qty);
    }

    @Test
    public void update_quantity_greater_than_product_stock_is_invalid() {
        // given
        setUp();
        Integer qty = 35;

        // when
        assertThatThrownBy(() -> cartItem.changeQty(qty))
            .isInstanceOf(NotEnoughStockException.class)
            .hasMessage(NOT_ENOUGH_STOCK_EXCEPTION);

    }

}