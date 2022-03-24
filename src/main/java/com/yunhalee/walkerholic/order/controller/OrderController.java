package com.yunhalee.walkerholic.order.controller;

import com.yunhalee.walkerholic.order.dto.CartResponse;
import com.yunhalee.walkerholic.order.dto.OrderRequest;
import com.yunhalee.walkerholic.order.dto.PayOrderRequest;
import com.yunhalee.walkerholic.order.dto.OrderResponse;
import com.yunhalee.walkerholic.order.dto.OrderResponses;
import com.yunhalee.walkerholic.order.dto.SimpleOrderResponse;
import com.yunhalee.walkerholic.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/users/{id}/orders")
    public ResponseEntity<OrderResponse> createOrder(@PathVariable("id") Integer id, @RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(id, request));
    }

    @PostMapping("/users/{id}/orders/cart")
    public ResponseEntity createCart(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(orderService.createCart(id));
    }

    @PutMapping("/orders/{id}/payment")
    public ResponseEntity payOrder(@PathVariable("id") Integer id, @RequestBody PayOrderRequest request) {
        orderService.payOrder(id, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/orders/{id}/delivery")
    public ResponseEntity<SimpleOrderResponse> deliverOrder(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(orderService.deliverOrder(id));
    }

    @PutMapping("/orders/{id}/cancellation")
    public ResponseEntity<SimpleOrderResponse> cancelOrder(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @GetMapping("/orders/{id}")
    public OrderResponse getOrder(@PathVariable("id") Integer id) {
        return orderService.getOrder(id);
    }

    @GetMapping("/users/{id}/orders/cart")
    public CartResponse getCart(@PathVariable("id") Integer id) {
        return orderService.getCart(id);
    }

    @GetMapping("/orders")
    public ResponseEntity<OrderResponses> getOrderList(@RequestParam("page") Integer page) {
        return ResponseEntity.ok(orderService.getOrderList(page));
    }

    @GetMapping("/users/{id}/orders/seller")
    public ResponseEntity<OrderResponses> getOrderListBySeller(@RequestParam("page") Integer page, @PathVariable("id") Integer id) {
        return ResponseEntity.ok(orderService.getOrderListBySeller(page, id));
    }

    @GetMapping("/users/{id}/orders")
    public ResponseEntity<OrderResponses> getOrderListByUser(@RequestParam("page") Integer page, @PathVariable("id") Integer id) {
        return ResponseEntity.ok(orderService.getOrderListByUser(page, id));
    }


}
