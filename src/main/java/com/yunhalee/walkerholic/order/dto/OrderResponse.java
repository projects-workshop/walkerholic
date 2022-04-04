package com.yunhalee.walkerholic.order.dto;

import com.yunhalee.walkerholic.common.dto.ItemResponse;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.user.dto.UserIconResponse;
import java.math.BigDecimal;
import java.util.List;
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
    private List<ItemResponse> items;
    private BigDecimal total;
    private BigDecimal shipping;

    private OrderResponse(Order order, UserIconResponse user, List<ItemResponse> items) {
        this.id = order.getId();
        this.orderStatus = order.getOrderStatus().name();
        this.paymentMethod = order.getPaymentMethod();
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
