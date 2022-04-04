package com.yunhalee.walkerholic.order.dto;

import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.dto.UserIconResponse;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
public class SimpleOrderResponse {

    private final Integer id;
    private final String orderStatus;
    private final LocalDateTime paidAt;
    private final boolean isDelivered;
    private final LocalDateTime deliveredAt;
    private final UserIconResponse user;
    private final BigDecimal totalAmount;


    private SimpleOrderResponse(Order order, UserIconResponse user) {
        this.id = order.getId();
        this.orderStatus = order.getOrderStatus().name();
        this.paidAt = order.getPaidAt();
        this.isDelivered = order.isDelivered();
        this.deliveredAt = order.getDeliveredAt();
        this.user = user;
        this.totalAmount = order.getTotalAmount();
    }

    public static SimpleOrderResponse of(Order order, UserIconResponse user) {
        return new SimpleOrderResponse(order, user);
    }

}
