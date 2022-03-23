package com.yunhalee.walkerholic.order.dto;

import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemResponses;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.dto.UserIconResponse;
import com.yunhalee.walkerholic.useractivity.dto.AddressDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
public class OrderResponse {

    private Integer id;
    private String orderStatus;
    private boolean isPaid;
    private String paymentMethod;
    private LocalDateTime paidAt;
    private boolean isDelivered;
    private LocalDateTime deliveredAt;
    private AddressDTO address;
    private UserIconResponse user;
    private OrderItemResponses orderItems;
    private Float total;
    private Float shipping;

    private OrderResponse(Order order, UserIconResponse user, OrderItemResponses orderItems) {
        this.id = order.getId();
        this.orderStatus = order.getOrderStatus().name();
        this.isPaid = order.isPaid();
        this.paymentMethod = order.getPaymentMethod();
        this.paidAt = order.getPaidAt();
        this.isDelivered = order.isDelivered();
        this.deliveredAt = order.getDeliveredAt();
        this.address = new AddressDTO(order.getAddress());
        this.user = user;
        this.orderItems = orderItems;
        this.total = order.getTotalAmount();
        this.shipping = order.getShipping();
    }

    public static OrderResponse of(Order order, UserIconResponse user, OrderItemResponses orderItems) {
        return new OrderResponse(order, user, orderItems);
    }
}
