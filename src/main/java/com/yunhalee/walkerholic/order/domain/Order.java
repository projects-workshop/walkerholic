package com.yunhalee.walkerholic.order.domain;

import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import com.yunhalee.walkerholic.user.domain.User;
import java.math.BigDecimal;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private OrderItems orderItems;

    private Order(OrderStatus orderStatus, User user) {
        this.orderStatus = orderStatus;
        this.user = user;
        this.orderItems = new OrderItems();
        this.deliveryInfo = new DeliveryInfo();
        this.paymentInfo = new PaymentInfo();
    }

    public Order(String paymentMethod, Address address, BigDecimal shipping) {
        this.deliveryInfo = new DeliveryInfo(address);
        this.paymentInfo = new PaymentInfo(shipping, paymentMethod);
        this.orderItems = new OrderItems();
    }

    public Order(Integer id, OrderStatus orderStatus, PaymentInfo paymentInfo, DeliveryInfo deliveryInfo, User user) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.paymentInfo = paymentInfo;
        this.deliveryInfo = deliveryInfo;
        this.user = user;
        this.orderItems = new OrderItems();
    }

    public static Order createCart(User user) {
        return new Order(OrderStatus.CART, user);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.addOrderItem(orderItem);
    }

    public void cancel() {
        deliveryInfo.checkAlreadyDelivered();
        this.orderStatus = OrderStatus.CANCEL;
        orderItems.cancelOrder();
    }

    public void deliver() {
        paymentInfo.checkOrderPaid();
        deliveryInfo.deliver();
    }

    public void pay(Order order) {
        orderItems.payOrder();
        this.deliveryInfo.changeAddress(order.getDeliveryInfo());
        this.orderStatus = OrderStatus.ORDER;
        paymentInfo.pay(order.getShipping(), order.getPaymentMethod());
    }

    public BigDecimal getShipping() {
        return paymentInfo.getShipping();
    }

    public String getPaymentMethod() {
        return paymentInfo.getPaymentMethod();
    }

    public boolean isPaid() {
        return paymentInfo.isPaid();
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
