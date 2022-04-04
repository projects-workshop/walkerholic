package com.yunhalee.walkerholic.order.domain;

import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import lombok.NonNull;

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
    private Payment payment;

    @Embedded
    private Delivery delivery;

    private Integer userId;

    @Embedded
    private OrderItems orderItems;

    @Builder
    public Order(Integer id, @NonNull OrderStatus orderStatus, @NonNull Payment payment, @NonNull Delivery delivery, @NonNull Integer userId, @NonNull OrderItems orderItems) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.payment = payment;
        this.delivery = delivery;
        this.userId = userId;
        this.orderItems = orderItems;
    }

    public static Order of(Integer userId, BigDecimal shipping, String paymentMethod, Address address) {
        return Order.builder()
            .userId(userId)
            .orderStatus(OrderStatus.ORDER)
            .delivery(Delivery.builder().isDelivered(false).address(address).build())
            .payment(new Payment(shipping, paymentMethod))
            .orderItems(new OrderItems())
            .build();
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.addOrderItem(orderItem);
    }

    public void cancel() {
        delivery.checkAlreadyDelivered();
        this.orderStatus = OrderStatus.CANCEL;
        orderItems.cancelOrder();
    }

    public void deliver() {
        delivery.deliver();
    }

    public BigDecimal getShipping() {
        return payment.getShipping();
    }

    public String getPaymentMethod() {
        return payment.getPaymentMethod();
    }

    public LocalDateTime getPaidAt() {
        return payment.getPaidAt();
    }

    public Set<OrderItem> getOrderItems() {
        return orderItems.getOrderItems();
    }

    public boolean isDelivered() {
        return delivery.isDelivered();
    }

    public LocalDateTime getDeliveredAt() {
        return delivery.getDeliveredAt();
    }

    public Address getAddress() {
        return delivery.getAddress();
    }

    @Transient
    public BigDecimal getTotalAmount() {
        return orderItems.getTotalAmount();
    }
}
