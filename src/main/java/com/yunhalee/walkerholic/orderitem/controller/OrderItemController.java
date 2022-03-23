package com.yunhalee.walkerholic.orderitem.controller;

import com.yunhalee.walkerholic.orderitem.dto.OrderItemRequest;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemResponse;
import com.yunhalee.walkerholic.orderitem.service.OrderItemService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderItemController {

    private final OrderItemService orderItemService;

    public OrderItemController(OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
    }

    @PostMapping("/orders/{id}/order-items")
    public ResponseEntity<OrderItemResponse> addToCart(@PathVariable("id") Integer id, @RequestBody OrderItemRequest request){
        return ResponseEntity.ok(orderItemService.create(id, request));
    }

    @PutMapping("/order-items/{id}")
    public ResponseEntity<OrderItemResponse> update(@PathVariable("id") Integer id, @Param("qty") Integer qty) {
        return ResponseEntity.ok(orderItemService.update(id, qty));
    }

    @DeleteMapping("/order-items/{id}")
    public ResponseEntity deleteOrderItem(@PathVariable("id") Integer id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }

}
