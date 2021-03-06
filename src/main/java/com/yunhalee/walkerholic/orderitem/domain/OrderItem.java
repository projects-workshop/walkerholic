package com.yunhalee.walkerholic.orderitem.domain;

import com.yunhalee.walkerholic.common.domain.Item;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.product.domain.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor
public class OrderItem extends Item {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;


    public OrderItem(Integer qty, Product product, Order order) {
        this.qty = qty;
        this.product = product;
        this.order = order;
    }

    public OrderItem(Integer id, Integer qty, Product product, Order order) {
        this.id = id;
        this.qty = qty;
        this.product = product;
        this.order = order;
    }

    public static OrderItem of(Integer qty, Product product, Order order) {
        OrderItem orderItem = new OrderItem(qty, product, order);
        order.addOrderItem(orderItem);
        orderItem.removeStock();
        return orderItem;
    }

    public void changeQty(Integer qty) {
        this.qty = qty;
    }

    public void cancel() {
        this.product.addStock(qty);
    }

    public void removeStock() {
        this.product.removeStock(qty);
    }

}
