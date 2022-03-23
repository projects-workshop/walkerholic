package com.yunhalee.walkerholic.order.domain;

import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.order.exception.OrderAlreadyDeliveredException;
import com.yunhalee.walkerholic.order.exception.OrderNotPaidException;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemRequest;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.useractivity.dto.AddressDTO;
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
    private boolean isPaid = false;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Column(name = "is_delivered")
    private boolean isDelivered = false;

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

    public Order(String paymentMethod, Address address, Float shipping) {
        this.paymentMethod = paymentMethod;
        this.address = address;
        this.shipping = shipping;
    }

    public Order(OrderStatus orderStatus, Float shipping, String paymentMethod,
        Address address, User user) {
        this.orderStatus = orderStatus;
        this.shipping = shipping;
        this.paymentMethod = paymentMethod;
        this.address = address;
        this.user = user;
    }

    public Order(OrderStatus orderStatus, Float shipping, boolean isPaid,
        String paymentMethod, Address address, User user) {
        this.orderStatus = orderStatus;
        this.shipping = shipping;
        this.isPaid = isPaid;
        this.paymentMethod = paymentMethod;
        this.address = address;
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


    public static Order createCart(User user) {
        return new Order(OrderStatus.CART, user);
    }

    public Order createOrder(){
        this.orderStatus = OrderStatus.ORDER;
        this.isPaid = true;
        return this;
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

    public void pay(Order order){
        orderItems.forEach(orderItem -> orderItem.payOrder());
        this.address = order.getAddress();
        this.shipping = order.shipping;
        this.paymentMethod = order.getPaymentMethod();
        this.isPaid = true;
        this.paidAt = LocalDateTime.now();
        this.orderStatus = OrderStatus.ORDER;
    }

}
