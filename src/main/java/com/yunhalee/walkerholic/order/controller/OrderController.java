package com.yunhalee.walkerholic.order.controller;

import com.yunhalee.walkerholic.order.dto.CartResponse;
import com.yunhalee.walkerholic.order.dto.OrderCreateDTO;
import com.yunhalee.walkerholic.order.dto.OrderResponse;
import com.yunhalee.walkerholic.order.dto.SimpleOrderResponse;
import com.yunhalee.walkerholic.order.service.OrderService;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemRequest;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemResponse;
import com.yunhalee.walkerholic.useractivity.dto.AddressDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/orders")
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateDTO orderCreateDTO) {
        AddressDTO addressDTO = orderCreateDTO.getAddress();
        List<OrderItemRequest> orderItemCreateDTOS = orderCreateDTO.getOrderItems();

        return new ResponseEntity<OrderResponse>(orderService.createOrder(orderCreateDTO),
            HttpStatus.CREATED);
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

    @GetMapping("/orderlist/{page}")
    public ResponseEntity<?> getOrderList(@PathVariable("page") String page) {
        Integer pageNumber = Integer.parseInt(page);
        return new ResponseEntity<HashMap<String, Object>>(orderService.getOrderList(pageNumber),
            HttpStatus.OK);
    }

    @GetMapping("/orderlistBySeller/{page}/{id}")
    public ResponseEntity<?> getOrderListBySeller(@PathVariable("page") String page,
        @PathVariable("id") String id) {
        Integer pageNumber = Integer.parseInt(page);
        Integer sellerId = Integer.parseInt(id);
        return new ResponseEntity<HashMap<String, Object>>(
            orderService.getOrderListBySeller(pageNumber, sellerId), HttpStatus.OK);
    }

    @GetMapping("/orderlistByUser/{page}/{id}")
    public ResponseEntity<?> getOrderListByUser(@PathVariable("page") String page,
        @PathVariable("id") String id) {
        Integer pageNumber = Integer.parseInt(page);
        Integer userId = Integer.parseInt(id);
        return new ResponseEntity<HashMap<String, Object>>(
            orderService.getOrderListByUser(pageNumber, userId), HttpStatus.OK);
    }

    @PostMapping("/payOrder")
    public void payOrder(@RequestBody OrderCreateDTO orderCreateDTO) {
        orderService.payOrder(orderCreateDTO);
    }


}
