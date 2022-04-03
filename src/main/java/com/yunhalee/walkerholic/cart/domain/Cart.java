package com.yunhalee.walkerholic.cart.domain;

import com.yunhalee.walkerholic.cartItem.domain.CartItem;
import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Cart extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Integer id;

    private Integer userId;

    @Embedded
    private CartItems cartItems;

    public Cart(Integer userId) {
        this.userId = userId;
        this.cartItems = new CartItems();
    }

    public static Cart of(Integer userId) {
        return new Cart(userId);
    }

    public void addCartItem(CartItem cartItem) {
        cartItems.addCartItem(cartItem);
    }

    public void emptyCart() {
        cartItems.emptyCart();
    }

    public Set<CartItem> getCartItems() {
        return cartItems.getCartItems();
    }

}