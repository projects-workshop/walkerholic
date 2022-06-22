package com.yunhalee.walkerholic.cartItem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartItemRequest {

    private Integer qty;
    private Integer productId;
    private Integer cartId;

    public CartItemRequest(Integer qty, Integer productId) {
        this.qty = qty;
        this.productId = productId;
    }

    public CartItemRequest(Integer qty, Integer productId, Integer cartId) {
        this.qty = qty;
        this.productId = productId;
        this.cartId = cartId;
    }
}
