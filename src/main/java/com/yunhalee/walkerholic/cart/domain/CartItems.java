package com.yunhalee.walkerholic.cart.domain;

import com.yunhalee.walkerholic.cartItem.domain.CartItem;
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
public class CartItems {

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<CartItem> cartItems = new HashSet<>();

    public CartItems() {
        this.cartItems = new HashSet<>();
    }

    public CartItems(CartItem... cartItems) {
        this.cartItems = new HashSet<>(Arrays.asList(cartItems));
    }

    public void addCartItem(CartItem cartItem) {
        this.cartItems.add(cartItem);
    }

    @Transient
    public BigDecimal getTotalAmount() {
        return cartItems.stream()
            .map(CartItem::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public Set<CartItem> getCartItems() {
        return Collections.unmodifiableSet(cartItems);
    }

    public void emptyCart() {
        this.cartItems.clear();
    }

    public boolean isEmpty() {
        return cartItems.size() == 0;
    }
}
