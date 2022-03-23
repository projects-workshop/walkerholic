package com.yunhalee.walkerholic.order.dto;

import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.dto.UserIconResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SimpleOrderResponse {

    private Integer id;

    private String orderStatus;

    private boolean isPaid;

    private LocalDateTime paidAt;

    private boolean isDelivered;

    private LocalDateTime deliveredAt;

    private UserIconResponse user;

    private Float totalAmount;


    private SimpleOrderResponse(Order order, UserIconResponse user) {
        this.id = order.getId();
        this.orderStatus = order.getOrderStatus().name();
        this.isPaid = order.isPaid();
        this.paidAt = order.getPaidAt();
        this.isDelivered = order.isDelivered();
        this.deliveredAt = order.getDeliveredAt();
        this.user = user;
        this.totalAmount = order.getTotalAmount();
    }

    public static SimpleOrderResponse of(Order order, UserIconResponse user){
        return new SimpleOrderResponse(order, user);
    }

}
