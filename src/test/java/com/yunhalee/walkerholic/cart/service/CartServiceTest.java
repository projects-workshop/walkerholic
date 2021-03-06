package com.yunhalee.walkerholic.cart.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.yunhalee.walkerholic.ServiceTest;
import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.cart.dto.CartResponse;
import com.yunhalee.walkerholic.cartItem.domain.CartItem;
import com.yunhalee.walkerholic.cartItem.domain.CartItemTest;
import com.yunhalee.walkerholic.common.dto.ItemResponse;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserTest;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class CartServiceTest extends ServiceTest {

    private static final String CART_ALREADY_EXIST_EXCEPTION = "Only one cart can be created per user.";

    @InjectMocks
    CartService cartService = new CartService(cartRepository, cartItemService);

    private Cart cart;
    private User user;
    private CartItem cartItem;

    @BeforeEach
    public void setUp() {
        user = UserTest.USER;
        cart = new Cart(1, user.getId());
        cartItem = CartItemTest.FIRST_CART_ITEM;
    }

    @Test
    public void create_cart_with_user_id() {
        // given

        // when
        when(cartRepository.findByUserId(any())).thenReturn(Optional.empty());
        when(cartRepository.save(any())).thenReturn(cart);
        Integer cartId = cartService.createCart(user.getId());

        // then
        assertThat(cartId).isEqualTo(cart.getId());
    }

    @Test
    public void empty_cart() {
        // given
        cart.addCartItem(cartItem);

        // when
        cartService.emptyCart(cart);

        // then
        assertThat(cart.getCartItems().size()).isEqualTo(0);
    }

    @Test
    public void find_cart_with_user_id() {
        // given
        cart.addCartItem(cartItem);

        // when
        when(cartRepository.findByUserId(any())).thenReturn(Optional.of(cart));
        when(cartItemService.cartItemResponses(any())).thenReturn(Arrays.asList(ItemResponse.of(cartItem)));
        CartResponse cartResponse = cartService.getCart(user.getId());

        // then
        assertThat(cartResponse.getId()).isEqualTo(cart.getId());
        assertThat(cartResponse.getItems().size()).isEqualTo(1);
    }


}