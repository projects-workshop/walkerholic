package com.yunhalee.walkerholic.cart.controller;

import com.yunhalee.walkerholic.cart.dto.CartResponse;
import com.yunhalee.walkerholic.cart.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    public ResponseEntity createCart(@RequestParam("userId") Integer userId) {
        return ResponseEntity.ok(cartService.createCart(userId));
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@RequestParam("userId") Integer userId) {
        return ResponseEntity.ok(cartService.getCart(userId));
    }


}
