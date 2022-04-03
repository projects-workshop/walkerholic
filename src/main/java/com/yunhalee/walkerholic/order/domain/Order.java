package com.yunhalee.walkerholic.order.domain;

import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import com.yunhalee.walkerholic.user.domain.User;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer id;

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Embedded
    private PaymentInfo paymentInfo;

    @Embedded
    private DeliveryInfo deliveryInfo;

    private Integer userId;

    @Embedded
    private OrderItems orderItems;

    @Builder
    public Order(Integer id, OrderStatus orderStatus, PaymentInfo paymentInfo, DeliveryInfo deliveryInfo, Integer userId, OrderItems orderItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.paymentInfo = paymentInfo;
        this.deliveryInfo = deliveryInfo;
        this.userId = userId;
        this.orderItems = orderItems;
    }

    public static Order of(Integer userId, BigDecimal shipping, String paymentMethod, Address address){
        return Order.builder()
            .userId(userId)
            .orderStatus(OrderStatus.ORDER)
            .deliveryInfo(DeliveryInfo.builder().isDelivered(false).address(address).build())
            .paymentInfo(new PaymentInfo(shipping, paymentMethod))
            .orderItems(new OrderItems())
            .build();
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.addOrderItem(orderItem);
    }

    public void cancel() {
        deliveryInfo.checkAlreadyDelivered();
        this.orderStatus = OrderStatus.CANCEL;
        orderItems.cancelOrder();
    }

    public void deliver() {
        deliveryInfo.deliver();
    }

    public BigDecimal getShipping() {
        return paymentInfo.getShipping();
    }

    public String getPaymentMethod() {
        return paymentInfo.getPaymentMethod();
    }

    public LocalDateTime getPaidAt() {
        return paymentInfo.getPaidAt();
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems.getOrderItems();
    }

    public boolean isDelivered() {
        return deliveryInfo.isDelivered();
    }

    public LocalDateTime getDeliveredAt() {
        return deliveryInfo.getDeliveredAt();
    }

    public Address getAddress() {
        return deliveryInfo.getAddress();
    }

    @Transient
    public BigDecimal getTotalAmount() {
        return orderItems.getTotalAmount();
    }
}
