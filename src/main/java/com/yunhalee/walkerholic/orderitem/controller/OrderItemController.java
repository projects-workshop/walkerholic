package com.yunhalee.walkerholic.orderitem.controller;

import com.yunhalee.walkerholic.orderitem.service.OrderItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteOrderItem(@PathVariable("id") Integer id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }

}
