package com.yunhalee.walkerholic.orderitem.dto;

import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class OrderItemCreateDTO {

    private Integer id;

    private Integer qty;

    private Integer productId;

    private Integer orderId;

    public OrderItemCreateDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.qty = orderItem.getQty();
        this.productId = orderItem.getProduct().getId();
        this.orderId = orderItem.getOrder().getId();
    }

    public OrderItemCreateDTO(Integer id, Integer qty, Integer productId, Integer orderId) {
        this.id = id;
        this.qty = qty;
        this.productId = productId;
        this.orderId = orderId;
    }

    public OrderItemCreateDTO(Integer qty, Integer productId, Integer orderId) {
        this.qty = qty;
        this.productId = productId;
        this.orderId = orderId;
    }
}
