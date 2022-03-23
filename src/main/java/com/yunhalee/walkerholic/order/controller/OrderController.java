package com.yunhalee.walkerholic.order.controller;

import com.yunhalee.walkerholic.order.dto.CartResponse;
import com.yunhalee.walkerholic.order.dto.OrderRequest;
import com.yunhalee.walkerholic.order.dto.PayOrderRequest;
import com.yunhalee.walkerholic.order.dto.OrderResponse;
import com.yunhalee.walkerholic.order.dto.OrderResponses;
import com.yunhalee.walkerholic.order.dto.SimpleOrderResponse;
import com.yunhalee.walkerholic.order.service.OrderService;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemRequest;
import com.yunhalee.walkerholic.useractivity.dto.AddressDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @PostMapping("/users/{id}/orders")
    public ResponseEntity createCart(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(orderService.createCart(id));
    }

    @PutMapping("/orders/{id}/cancel")
    public ResponseEntity<SimpleOrderResponse> cancelOrder(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @PutMapping("/orders/{id}/deliver")
    public ResponseEntity<SimpleOrderResponse> deliverOrder(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(orderService.deliverOrder(id));
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
    public ResponseEntity<OrderResponses> getOrderListByUser(@RequestParam("page") Integer page,
        @PathVariable("id") Integer id) {
        return ResponseEntity.ok(orderService.getOrderListByUser(page, id));
    }

    @PutMapping("/orders/{id}/pay")
    public ResponseEntity payOrder(@PathVariable("id") Integer id, @RequestBody PayOrderRequest request) {
        orderService.payOrder(id, request);
        return ResponseEntity.ok().build();
    }


}
