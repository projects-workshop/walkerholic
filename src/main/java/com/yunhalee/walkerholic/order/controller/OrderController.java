package com.yunhalee.walkerholic.order.controller;

import com.yunhalee.walkerholic.order.dto.OrderRequest;
import com.yunhalee.walkerholic.order.dto.OrderResponse;
import com.yunhalee.walkerholic.order.dto.OrderResponses;
import com.yunhalee.walkerholic.order.dto.SimpleOrderResponse;
import com.yunhalee.walkerholic.order.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest request) {
        return ResponseEntity.ok(orderService.createOrder(request));
    }

    @PutMapping("/{id}/delivery")
    public ResponseEntity<SimpleOrderResponse> deliverOrder(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(orderService.deliverOrder(id));
    }

    @PutMapping("/{id}/cancellation")
    public ResponseEntity<SimpleOrderResponse> cancelOrder(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(orderService.cancelOrder(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @GetMapping( params = "page")
    public ResponseEntity<OrderResponses> getOrderList(@RequestParam("page") Integer page) {
        return ResponseEntity.ok(orderService.getOrderList(page));
    }

    @GetMapping( params = {"page", "sellerId"})
    public ResponseEntity<OrderResponses> getOrderListBySeller(@RequestParam("page") Integer page, @RequestParam("sellerId") Integer sellerId) {
        return ResponseEntity.ok(orderService.getOrderListBySeller(page, sellerId));
    }

    @GetMapping( params = {"page", "userId"})
    public ResponseEntity<OrderResponses> getOrderListByUser(@RequestParam("page") Integer page, @RequestParam("userId") Integer userId) {
        return ResponseEntity.ok(orderService.getOrderListByUser(page, userId));
    }


}
