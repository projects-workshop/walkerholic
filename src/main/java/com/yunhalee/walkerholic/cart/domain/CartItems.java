package com.yunhalee.walkerholic.cart.domain;

import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

@Embeddable
public class CartItems {

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems = new HashSet<>();

    public CartItems() {
        this.orderItems = new HashSet<>();
    }

    public CartItems(OrderItem... orderItems) {
        this.orderItems = new HashSet<>(Arrays.asList(orderItems));
    }

    public void addOrderItem(OrderItem orderItem) {
        this.orderItems.add(orderItem);
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

    public void emptyOrderItems() {
        this.orderItems.clear();
    }

}
