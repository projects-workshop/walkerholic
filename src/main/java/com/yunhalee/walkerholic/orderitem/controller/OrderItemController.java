package com.yunhalee.walkerholic.orderitem.controller;

import com.yunhalee.walkerholic.orderitem.service.OrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OrderItemController {

    private final OrderItemService orderItemService;

    @PostMapping("/updateQty/{id}")
    public void updateQty(@PathVariable("id") String id, @Param("qty") String qty) {
        Integer orderItemId = Integer.parseInt(id);
        Integer orderItemQty = Integer.parseInt(qty);
        orderItemService.updateQty(orderItemId, orderItemQty);
        return;
    }

    @DeleteMapping("/deleteOrderItem/{id}")
    public void deleteOrderItem(@PathVariable("id") String id) {
        Integer orderItemId = Integer.parseInt(id);
        orderItemService.deleteOrderItem(orderItemId);
        return;
    }

}
