package com.yunhalee.walkerholic.cart.dto;

import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.common.dto.ItemResponses;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartResponse {

    private Integer id;

    private ItemResponses cartItems;

    public CartResponse(Cart cart, ItemResponses cartItems) {
        this.id = cart.getId();
        this.cartItems = cartItems;
    }

    public static CartResponse of(Cart cart, ItemResponses cartItems) {
        return new CartResponse(cart, cartItems);
    }

}
