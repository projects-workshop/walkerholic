package com.yunhalee.walkerholic.order.domain;

import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Embeddable
public class OrderItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    public OrderItems() {
        this.orderItems = new HashSet<>();
    }

    public OrderItems(OrderItem... orderItems) {
        this.orderItems = new HashSet<>(Arrays.asList(orderItems));
    }

    public OrderItems(Set<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
    }

    public void cancelOrder() {
        orderItems.stream()
            .forEach(orderItem -> orderItem.cancel());
    }

    @Transient
    public BigDecimal getTotalAmount() {
        return orderItems.stream()
            .map(OrderItem::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Set<OrderItem> getOrderItems() {
        return Collections.unmodifiableSet(orderItems);
    }


}
