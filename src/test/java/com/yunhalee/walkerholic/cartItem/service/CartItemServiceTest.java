package com.yunhalee.walkerholic.cartItem.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.cart.domain.CartTest;
import com.yunhalee.walkerholic.cartItem.domain.CartItem;
import com.yunhalee.walkerholic.cartItem.dto.CartItemRequest;
import com.yunhalee.walkerholic.cartItem.dto.CartItemResponse;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.domain.ProductTest;
import com.yunhalee.walkerholic.product.exception.NotEnoughStockException;
import com.yunhalee.walkerholic.productImage.domain.ProductImageTest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;


class CartItemServiceTest extends MockBeans {

    private static final String NOT_ENOUGH_STOCK_EXCEPTION = "Stock is not enough.";

    @InjectMocks
    CartItemService cartItemService = new CartItemService(
        cartRepository,
        productService,
        cartItemRepository
    );

    private CartItem cartItem;
    private Product product;
    private Cart cart;


    @BeforeEach
    public void setUp(){
        cart = CartTest.CART;
        product = ProductTest.FIRST_PRODUCT;
        product.addProductImage(ProductImageTest.PRODUCT_IMAGE);
        cartItem = CartItem.builder()
            .id(1)
            .qty(3)
            .product(product)
            .cart(cart).build();
    }

    @Test
    public void create_cart_item(){
        //given
        CartItemRequest request = new CartItemRequest(cartItem.getQty(), product.getId(), cart.getId());

        //when
        when(cartRepository.findById(anyInt())).thenReturn(Optional.of(cart));
        when(productService.findProductById(anyInt())).thenReturn(product);
        when(cartItemRepository.save(any())).thenReturn(cartItem);
        CartItemResponse response = cartItemService.create(request);

        //then
        assertThat(response.getId()).isEqualTo(cartItem.getId());
        assertThat(response.getQty()).isEqualTo(cartItem.getQty());
        assertThat(response.getProductId()).isEqualTo(product.getId());
        assertThat(cart.getCartItems().size()).isEqualTo(1);
    }


    @Test
    public void update_qty() {
        //given
        Integer qty = 2;

        //when
        when(cartItemRepository.findById(anyInt())).thenReturn(Optional.of(cartItem));
        CartItemResponse response = cartItemService.update(cartItem.getId(), qty);

        //then
        assertThat(response.getQty()).isEqualTo(qty);
    }

    @Test
    public void update_qty_more_then_product_stock_is_invalid(){
        Integer qty = 200;

        when(cartItemRepository.findById(anyInt())).thenReturn(Optional.of(cartItem));
        assertThatThrownBy(()->cartItemService.update(cartItem.getId(), qty))
            .isInstanceOf(NotEnoughStockException.class)
            .hasMessage(NOT_ENOUGH_STOCK_EXCEPTION);

    }

    @Test
    public void delete_cart_item() {
        //given

        //when
        when(cartItemRepository.findById(anyInt())).thenReturn(Optional.of(cartItem));
        cartItemService.deleteCartItem(cartItem.getId());

        //then
        verify(cartItemRepository).delete(any());
    }

    @Test
    public void empty_cart() {
        //given

        //when
        cartItemService.emptyCart(cart);

        //then
        verify(cartItemRepository).deleteAllByCart(any());
    }

}