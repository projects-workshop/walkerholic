package com.yunhalee.walkerholic.order.dto;

import com.yunhalee.walkerholic.common.dto.ItemResponses;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.user.dto.UserIconResponse;
import java.math.BigDecimal;
import lombok.Getter;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderResponse {

    private Integer id;
    private String orderStatus;
    private String paymentMethod;
    private LocalDateTime paidAt;
    private boolean isDelivered;
    private LocalDateTime deliveredAt;
    private AddressResponse address;
    private UserIconResponse user;
    private ItemResponses orderItems;
    private BigDecimal total;
    private BigDecimal shipping;

    private OrderResponse(Order order, UserIconResponse user, ItemResponses orderItems) {
        this.id = order.getId();
        this.orderStatus = order.getOrderStatus().name();
        this.paymentMethod = order.getPaymentMethod();
        this.paidAt = order.getPaidAt();
        this.isDelivered = order.isDelivered();
        this.deliveredAt = order.getDeliveredAt();
        this.address = new AddressResponse(order.getAddress());
        this.user = user;
        this.orderItems = orderItems;
        this.total = order.getTotalAmount();
        this.shipping = order.getShipping();
    }

    public static OrderResponse of(Order order, UserIconResponse user, ItemResponses orderItems) {
        return new OrderResponse(order, user, orderItems);
    }
}
