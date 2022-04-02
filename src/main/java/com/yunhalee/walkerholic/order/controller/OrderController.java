package com.yunhalee.walkerholic.order.controller;

import com.yunhalee.walkerholic.common.dto.PageSortRequest;
import com.yunhalee.walkerholic.order.dto.OrderRequest;
import com.yunhalee.walkerholic.order.dto.OrderResponse;
import com.yunhalee.walkerholic.order.dto.OrderResponses;
import com.yunhalee.walkerholic.order.dto.SimpleOrderResponse;
import com.yunhalee.walkerholic.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
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

    @GetMapping("/orders")
    public ResponseEntity<OrderResponses> getOrderList(@RequestParam("page") Integer page) {
        return ResponseEntity.ok(orderService.getOrderList(page));
    }

    @GetMapping("/orders")
    public ResponseEntity<OrderResponses> getOrderListBySeller(@RequestParam("page") Integer page, @RequestParam("sellerId") Integer sellerId) {
        return ResponseEntity.ok(orderService.getOrderListBySeller(page, sellerId));
    }

    @GetMapping("/orders")
    public ResponseEntity<OrderResponses> getOrderListByUser(@RequestParam("page") Integer page, @RequestParam("userId") Integer userId) {
        return ResponseEntity.ok(orderService.getOrderListByUser(page, userId));
    }


}
