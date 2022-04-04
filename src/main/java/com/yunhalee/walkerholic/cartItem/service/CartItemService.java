package com.yunhalee.walkerholic.cartItem.service;

import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.cart.domain.CartRepository;
import com.yunhalee.walkerholic.cartItem.domain.CartItem;
import com.yunhalee.walkerholic.cartItem.domain.CartItemRepository;
import com.yunhalee.walkerholic.cartItem.dto.CartItemRequest;
import com.yunhalee.walkerholic.common.dto.ItemResponse;
import com.yunhalee.walkerholic.common.dto.ItemResponses;
import com.yunhalee.walkerholic.cartItem.exception.CartItemNotFoundException;
import com.yunhalee.walkerholic.order.exception.OrderNotFoundException;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.service.ProductService;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    private CartRepository cartRepository;
    private ProductService productService;
    private CartItemRepository cartItemRepository;

    public CartItemService(CartRepository cartRepository, ProductService productService, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.cartItemRepository = cartItemRepository;
    }

    public ItemResponse create(CartItemRequest request) {
        Cart cart = cartRepository.findById(request.getCartId())
            .orElseThrow(() -> new OrderNotFoundException("Cart not found with id : " + request.getCartId()));
        Product product = productService.findProductById(request.getProductId());
        CartItem cartItem = cartItemRepository.save(CartItem.of(request.getQty(), product, cart));
        return ItemResponse.of(cartItem);
    }

    public ItemResponse update(Integer id, Integer qty) {
        CartItem cartItem = findCartItemById(id);
        cartItem.changeQty(qty);
        return ItemResponse.of(cartItem);
    }

    public void emptyCart(Cart cart) {
        cartItemRepository.deleteAllByCart(cart);
    }

    public void deleteCartItem(Integer id) {
        CartItem cartItem = findCartItemById(id);
        cartItemRepository.delete(cartItem);
    }

    public CartItem findCartItemById(Integer id) {
        return cartItemRepository.findById(id)
            .orElseThrow(() -> new CartItemNotFoundException("CartItem not found with id : " + id));
    }


    public ItemResponses cartItemResponses(Set<CartItem> cartItems) {
        return ItemResponses.of(
            cartItems.stream()
                .map(ItemResponse::of)
                .collect(Collectors.toList()));
    }


}
