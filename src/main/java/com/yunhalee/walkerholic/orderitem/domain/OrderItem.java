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

    public BigDecimal getAmount() {
        return product.getPrice().multiply(BigDecimal.valueOf(qty));
    }

    public Integer getProductStock() {
        return this.product.getStock();
    }

    public Integer getProductId() {
        return this.product.getId();
    }

    public String getProductName() {
        return this.product.getName();
    }

    public BigDecimal getProductPrice() {
        return this.product.getPrice();
    }

    public String getProductDescription() {
        return this.product.getDescription();
    }

    public String getProductBrand() {
        return this.product.getBrand();
    }

    public String getProductImageUrl() {
        return this.product.getMainImageUrl();
    }


}
