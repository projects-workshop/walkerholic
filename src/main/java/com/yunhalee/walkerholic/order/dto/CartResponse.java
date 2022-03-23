package com.yunhalee.walkerholic.order.dto;

import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemResponses;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartResponse {

    private Integer id;

    private OrderItemResponses orderItems;

    public CartResponse(Order order, OrderItemResponses orderItems) {
        this.id = order.getId();
        this.orderItems = orderItems;
    }

    public static CartResponse of(Order order, OrderItemResponses orderItems){
        return new CartResponse(order, orderItems);
    }

}
