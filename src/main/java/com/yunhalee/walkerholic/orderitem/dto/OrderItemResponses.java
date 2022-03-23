package com.yunhalee.walkerholic.orderitem.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderItemResponses {

    private List<OrderItemResponse> orderItems;

    private OrderItemResponses(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }

    public static OrderItemResponses of(List<OrderItemResponse> orderItems){
        return new OrderItemResponses(orderItems);
    }
}
