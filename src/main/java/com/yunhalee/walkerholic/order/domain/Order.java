package com.yunhalee.walkerholic.order.domain;

import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.order.exception.OrderAlreadyDeliveredException;
import com.yunhalee.walkerholic.order.exception.OrderNotPaidException;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import com.yunhalee.walkerholic.user.domain.User;
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

    private Float shipping;

    @Column(name = "is_paid")
    private boolean isPaid;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "is_delivered")
    private boolean isDelivered;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Embedded
    private Address address;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    public Order(OrderStatus orderStatus, User user) {
        this.orderStatus = orderStatus;
        this.user = user;
    }

    @Transient
    public Float getTotalAmount() {
        Float totalAmount = 0f;
        for (OrderItem orderItem : this.orderItems) {
            totalAmount += orderItem.getProduct().getPrice().floatValue() * orderItem.getQty();
        }
        return totalAmount;
    }

    //연관관계메서드
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
    }

    //비지니스 로직
    public static Order createOrder(User user, Address address, List<OrderItem> orderItems,
        String paymentMethod) {
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        orderItems.forEach(orderItem -> {
            order.addOrderItem(orderItem);
        });
        order.setOrderStatus(OrderStatus.ORDER);
        order.setPaymentMethod(paymentMethod);
        return order;
    }

    public static Order createCart(User user) {
        return new Order(OrderStatus.CART, user);
    }

    public void cancel() {
        if (isDelivered) {
            throw new OrderAlreadyDeliveredException("Order Already Completed. All the items has been delivered.");
        }
        this.orderStatus = OrderStatus.CANCEL;
        orderItems.forEach(orderItem -> {
            orderItem.cancel();
        });
    }

    public void deliver() {
        if (!isPaid) {
            throw new OrderNotPaidException("Order must be paid.");
        }
        this.isDelivered=true;
        this.deliveredAt=LocalDateTime.now();
    }

}
