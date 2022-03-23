package com.yunhalee.walkerholic.orderitem.dto;

import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
public class OrderItemRequest {

    private Integer qty;
    private Integer productId;
    private Integer orderId;

    public OrderItemRequest(OrderItem orderItem) {
        this.qty = orderItem.getQty();
        this.productId = orderItem.getProduct().getId();
        this.orderId = orderItem.getOrder().getId();
    }

    public OrderItemRequest(Integer qty, Integer productId, Integer orderId) {
        this.qty = qty;
        this.productId = productId;
        this.orderId = orderId;
    }
}
