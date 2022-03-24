package com.yunhalee.walkerholic.orderitem.domain;

import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.product.domain.Product;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "order_item")
@Getter
@NoArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Integer id;

    @Column(nullable = false)
    private Integer qty;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    public OrderItem(Integer qty, Product product) {
        this.qty = qty;
        this.product = product;
    }

    public OrderItem(Integer qty, Product product, Order order) {
        this.qty = qty;
        this.product = product;
        this.order = order;
    }

    public OrderItem(Integer id, Integer qty, Product product) {
        this.id = id;
        this.qty = qty;
        this.product = product;
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
        return orderItem;
    }

    public void changeQty(Integer qty) {
        this.qty = qty;
    }

    public void cancel() {
        this.product.addStock(qty);
    }

    public void payOrder() {
        this.product.removeStock(qty);
    }

    public BigDecimal getAmount() {
        return product.getPrice().multiply(BigDecimal.valueOf(qty));
    }


}
