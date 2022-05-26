package com.yunhalee.walkerholic.order.domain;

import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.*;
import lombok.NonNull;

@Entity
@Table(name = "orders", indexes = @Index(name = "idx_userId_timeSeparator", columnList = "user_id, time_separator", unique = true))
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

    @Column(name = "user_id")
    private Integer userId;

    @Embedded
    private OrderItems orderItems;

    @Column(name = "time_separator")
    private String timeSeparator = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

    @Builder
    public Order(Integer id, @NonNull OrderStatus orderStatus, @NonNull Payment payment, @NonNull Delivery delivery, @NonNull Integer userId, @NonNull OrderItems orderItems, String timeSeparator) {
        this.id = id;
        this.orderStatus = orderStatus;
        this.payment = payment;
        this.delivery = delivery;
        this.userId = userId;
        this.orderItems = orderItems;
        this.timeSeparator = timeSeparator;
    }

    public static Order of(Integer userId, BigDecimal shipping, String paymentMethod, String transactionId, Address address) {
        return Order.builder()
            .userId(userId)
            .orderStatus(OrderStatus.ORDER)
            .delivery(Delivery.builder().isDelivered(false).address(address).build())
            .payment(new Payment(shipping, paymentMethod, transactionId))
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

    public String getTransactionId() {
        return payment.getTransactionId();
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
