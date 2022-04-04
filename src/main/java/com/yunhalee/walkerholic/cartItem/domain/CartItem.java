package com.yunhalee.walkerholic.cartItem.domain;

import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.exception.NotEnoughStockException;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "cart_item")
@Getter
@NoArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_item_id")
    private Integer id;

    @Column(nullable = false)
    private Integer qty;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Builder
    public CartItem(Integer id, @NonNull Integer qty, Product product, Cart cart) {
        this.id = id;
        this.qty = qty;
        this.product = product;
        this.cart = cart;
    }

    public static CartItem of(Integer qty, Product product, Cart cart) {
        CartItem cartItem = CartItem.builder()
            .qty(qty)
            .product(product)
            .cart(cart).build();
        cart.addCartItem(cartItem);
        return cartItem;
    }

    public void changeQty(Integer qty) {
        if (!this.product.isEnoughStock(qty)) {
            throw new NotEnoughStockException("Stock is not enough.");
        }
        this.qty = qty;
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


