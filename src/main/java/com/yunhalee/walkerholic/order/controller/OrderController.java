package com.yunhalee.walkerholic.order.controller;

import com.yunhalee.walkerholic.order.dto.OrderCartDTO;
import com.yunhalee.walkerholic.order.dto.OrderCreateDTO;
import com.yunhalee.walkerholic.order.dto.OrderDTO;
import com.yunhalee.walkerholic.order.dto.OrderListDTO;
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

    @PostMapping("/order/create")
    public ResponseEntity<?> createOrder(@RequestBody OrderCreateDTO orderCreateDTO) {
        AddressDTO addressDTO = orderCreateDTO.getAddress();
        List<OrderItemRequest> orderItemCreateDTOS = orderCreateDTO.getOrderItems();

        return new ResponseEntity<OrderDTO>(orderService.createOrder(orderCreateDTO),
            HttpStatus.CREATED);
    }

    @PostMapping("/order/cancel/{id}")
    public OrderListDTO deleteOrder(@PathVariable("id") String id) {
        Integer orderId = Integer.parseInt(id);
        return orderService.cancelOrder(orderId);
    }

    @PostMapping("/order/deliver/{id}")
    public OrderListDTO deliverOrder(@PathVariable("id") String id) {
        Integer orderId = Integer.parseInt(id);
        return orderService.deliverOrder(orderId);
    }

    @GetMapping("/getOrder/{id}")
    public OrderDTO getOrder(@PathVariable("id") String id) {
        Integer orderId = Integer.parseInt(id);
        return orderService.getOrder(orderId);
    }

    @GetMapping("/cartItems/{id}")
    public OrderCartDTO getCart(@PathVariable("id") String id) {
        Integer userId = Integer.parseInt(id);
        return orderService.getCart(userId);
    }

    @PostMapping("/createCart/{id}")
    public Integer createCart(@PathVariable("id") String id) {
        Integer userId = Integer.parseInt(id);
        return orderService.createCart(userId);
    }

    @PostMapping("/addToCart/{id}")
    public OrderItemResponse addToCart(@PathVariable("id") String id,
        @RequestBody OrderItemRequest orderItem) {
        Integer orderId = Integer.parseInt(id);
        System.out.println(orderItem);
        return orderService.addToCart(orderId, orderItem);
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
