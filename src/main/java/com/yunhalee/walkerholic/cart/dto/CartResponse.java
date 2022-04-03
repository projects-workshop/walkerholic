package com.yunhalee.walkerholic.cart.dto;

import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.cartItem.dto.CartItemResponses;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemResponses;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartResponse {

    private Integer id;

    private CartItemResponses cartItems;

    public CartResponse(Cart cart, CartItemResponses cartItems) {
        this.id = cart.getId();
        this.cartItems = cartItems;
    }

    public static CartResponse of(Cart cart, CartItemResponses cartItems){
        return new CartResponse(cart, cartItems);
    }

}
