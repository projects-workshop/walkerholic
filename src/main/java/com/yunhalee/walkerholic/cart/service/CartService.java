package com.yunhalee.walkerholic.cart.service;

import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.cart.domain.CartRepository;
import com.yunhalee.walkerholic.cart.exception.CartAlreadyExist;
import com.yunhalee.walkerholic.cart.exception.CartNotFoundException;
import com.yunhalee.walkerholic.cart.dto.CartResponse;
import com.yunhalee.walkerholic.cartItem.service.CartItemService;
import com.yunhalee.walkerholic.orderitem.service.OrderItemService;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CartService {

    private CartRepository cartRepository;

    private CartItemService cartItemService;

    public CartService(CartRepository cartRepository,
        CartItemService cartItemService) {
        this.cartRepository = cartRepository;
        this.cartItemService = cartItemService;
    }


    public Integer createCart(Integer userId) {
        checkCartExist(userId);
        Cart cart = cartRepository.save(Cart.of(userId));
        return cart.getId();
    }


    private void checkCartExist(Integer userId) {
        if (cartRepository.existsByUserId(userId)) {
            throw new CartAlreadyExist("Only one cart can be created per user.");
        }
    }

    public void emptyCart(Integer userId) {
        Cart cart = findCartByUserId(userId);
        cartItemService.deleteAllByCartId(cart.getId());
    }



    @Transactional(readOnly = true)
    public CartResponse getCart(Integer userId) {
        Optional<Cart> cart = cartRepository.findByUserId(userId);
        if (cart.isPresent()) {
            return new CartResponse(cart.get(),
                cartItemService.cartItemResponses(cart.get().getCartItems()));
        }
        return new CartResponse();
    }

    public Cart findCartByUserId(Integer userId) {
        return cartRepository.findByUserId(userId)
            .orElseThrow(() -> new CartNotFoundException("Cart not found with user id : " + userId));
    }


}
