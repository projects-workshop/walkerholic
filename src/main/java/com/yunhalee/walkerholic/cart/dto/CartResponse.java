package com.yunhalee.walkerholic.cart.dto;

import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemResponses;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartResponse {

    private Integer id;

    private OrderItemResponses orderItems;

    public CartResponse(Cart cart, OrderItemResponses orderItems) {
        this.id = cart.getId();
        this.orderItems = orderItems;
    }

    public static CartResponse of(Cart cart, OrderItemResponses orderItems){
        return new CartResponse(cart, orderItems);
    }

}
