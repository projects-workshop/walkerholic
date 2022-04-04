package com.yunhalee.walkerholic.cartItem.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class CartItemResponses {

    private List<CartItemResponse> cartItems = new ArrayList<>();

    public CartItemResponses() {
        this.cartItems = new ArrayList<>();
    }

    private CartItemResponses(List<CartItemResponse> cartItems) {
        this.cartItems = cartItems;
    }

    public static CartItemResponses of(List<CartItemResponse> cartItems) {
        return new CartItemResponses(cartItems);
    }


}
