package com.yunhalee.walkerholic.cartItem.domain;

import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.common.domain.Item;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.exception.NotEnoughStockException;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "cart_item")
@Getter
@NoArgsConstructor
public class CartItem extends Item {

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

}


