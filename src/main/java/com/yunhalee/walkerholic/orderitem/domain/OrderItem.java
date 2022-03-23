package com.yunhalee.walkerholic.orderitem.domain;

import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.product.domain.Product;
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

    public static OrderItem createOrderItem(Product product, Integer qty) {
        OrderItem orderItem = new OrderItem(qty, product);
        product.removeStock(qty);
        return orderItem;
    }

    public void changeQty (Integer qty){
        this.qty = qty;
    }

    public void cancel() {
        getProduct().addStock(qty);
    }


}
