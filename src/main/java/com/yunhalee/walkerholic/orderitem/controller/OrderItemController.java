package com.yunhalee.walkerholic.orderitem.controller;

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

    @PostMapping("/order-items/{id}")
    public ResponseEntity update(@PathVariable("id") Integer id, @Param("qty") Integer qty) {
        orderItemService.update(id, qty);
        return ResponseEntity.created(URI.create("/order-items/" + id)).build();
    }

    @DeleteMapping("/order-items/{id}")
    public ResponseEntity deleteOrderItem(@PathVariable("id") Integer id) {
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }

}
