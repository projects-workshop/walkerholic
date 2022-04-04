package com.yunhalee.walkerholic.order.dto;

import com.yunhalee.walkerholic.common.dto.ItemResponse;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.user.dto.UserIconResponse;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderResponse {

    private final Integer id;
    private final String orderStatus;
    private final String paymentMethod;
    private final String transactionId;
    private final LocalDateTime paidAt;
    private final boolean isDelivered;
    private final LocalDateTime deliveredAt;
    private final AddressResponse address;
    private final UserIconResponse user;
    private final List<ItemResponse> items;
    private final BigDecimal total;
    private final BigDecimal shipping;

    private OrderResponse(Order order, UserIconResponse user, List<ItemResponse> items) {
        this.id = order.getId();
        this.orderStatus = order.getOrderStatus().name();
        this.paymentMethod = order.getPaymentMethod();
        this.transactionId = order.getTransactionId();
        this.paidAt = order.getPaidAt();
        this.isDelivered = order.isDelivered();
        this.deliveredAt = order.getDeliveredAt();
        this.address = new AddressResponse(order.getAddress());
        this.user = user;
        this.items = items;
        this.total = order.getTotalAmount();
        this.shipping = order.getShipping();
    }

    public static OrderResponse of(Order order, UserIconResponse user, List<ItemResponse> items) {
        return new OrderResponse(order, user, items);
    }
}
