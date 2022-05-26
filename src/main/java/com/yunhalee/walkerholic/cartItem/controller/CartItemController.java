package com.yunhalee.walkerholic.cartItem.controller;

import com.yunhalee.walkerholic.cartItem.dto.CartItemRequest;
import com.yunhalee.walkerholic.common.dto.ItemResponse;
import com.yunhalee.walkerholic.cartItem.service.CartItemService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CartItemController {

    private final CartItemService cartItemService;

    public CartItemController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }

    @PostMapping("/cart-items")
    public ResponseEntity<ItemResponse> createCartItem(@RequestBody CartItemRequest request) {
        return ResponseEntity.ok(cartItemService.create(request));
    }

    @PutMapping("/cart-items/{id}")
    public ResponseEntity<ItemResponse> updateQty(@PathVariable("id") Integer id, @Param("qty") Integer qty) {
        return ResponseEntity.ok(cartItemService.update(id, qty));
    }

    @DeleteMapping("/cart-items/{id}")
    public ResponseEntity deleteCartItem(@PathVariable("id") Integer id) {
        cartItemService.deleteCartItem(id);
        return ResponseEntity.noContent().build();
    }
}
