package com.yunhalee.walkerholic.order.dto;

import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class OrderListDTO {

    private Integer id;

    private String orderStatus;

    private boolean isPaid;

    private LocalDateTime paidAt;

    private boolean isDelivered;

    private LocalDateTime deliveredAt;

    private OrderUser user;

    private Float totalAmount;


    public OrderListDTO(Order order) {
        this.id = order.getId();
        this.orderStatus = order.getOrderStatus().name();
        this.isPaid = order.isPaid();
        this.paidAt = order.getPaidAt();
        this.isDelivered = order.isDelivered();
        this.deliveredAt = order.getDeliveredAt();
        this.user = new OrderUser(order.getUser());
        this.totalAmount = order.getTotalAmount();
    }

    @Getter
    static class OrderUser {

        private Integer id;
        private String fullname;
        private String imageUrl;

        public OrderUser(User user) {
            this.id = user.getId();
            this.fullname = user.getFullname();
            this.imageUrl = user.getImageUrl();
        }
    }


}
