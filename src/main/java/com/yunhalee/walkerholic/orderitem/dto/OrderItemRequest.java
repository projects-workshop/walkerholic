package com.yunhalee.walkerholic.orderitem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
public class OrderItemRequest {

    private Integer qty;
    private Integer productId;
    private Integer orderId;

    public OrderItemRequest(Integer qty, Integer productId) {
        this.qty = qty;
        this.productId = productId;
    }

    public OrderItemRequest(Integer qty, Integer productId, Integer orderId) {
        this.qty = qty;
        this.productId = productId;
        this.orderId = orderId;
    }
}
