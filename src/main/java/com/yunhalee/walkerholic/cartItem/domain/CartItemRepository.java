package com.yunhalee.walkerholic.cartItem.domain;

import com.yunhalee.walkerholic.cart.domain.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    void deleteAllByCart(Cart cart);
}
