package com.yunhalee.walkerholic.orderitem.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class OrderItemResponses {

    private List<OrderItemResponse> orderItems = new ArrayList<>();

    public OrderItemResponses() {
        this.orderItems = new ArrayList<>();
    }

    private OrderItemResponses(List<OrderItemResponse> orderItems) {
        this.orderItems = orderItems;
    }

    public static OrderItemResponses of(List<OrderItemResponse> orderItems){
        return new OrderItemResponses(orderItems);
    }
}
