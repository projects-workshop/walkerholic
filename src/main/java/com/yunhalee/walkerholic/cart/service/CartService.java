package com.yunhalee.walkerholic.cart.service;

import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.cart.domain.CartRepository;
import com.yunhalee.walkerholic.cart.exception.CartAlreadyExist;
import com.yunhalee.walkerholic.cart.exception.CartNotFoundException;
import com.yunhalee.walkerholic.cart.dto.CartResponse;
import com.yunhalee.walkerholic.cartItem.service.CartItemService;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CartService {

    private CartRepository cartRepository;

    private CartItemService cartItemService;

    public CartService(CartRepository cartRepository, CartItemService cartItemService) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
    }

    public Integer createCart(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId)
            .orElseGet(() -> cartRepository.save(Cart.of(userId)));
        return cart.getId();
    }

    public void emptyCart(Cart cart) {
        cartItemService.emptyCart(cart);
        cart.emptyCart();
    }

    @Transactional(readOnly = true)
    public CartResponse getCart(Integer userId) {
        Cart cart = cartRepository.findByUserId(userId)
            .orElseGet(() -> new Cart());
        return CartResponse.of(cart, cartItemService.cartItemResponses(cart.getCartItems()));
    }

    public Cart findCartByUserId(Integer userId) {
        return cartRepository.findByUserId(userId)
            .orElseThrow(
                () -> new CartNotFoundException("Cart not found with user id : " + userId));
    }


}
