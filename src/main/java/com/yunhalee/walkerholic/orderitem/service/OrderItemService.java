package com.yunhalee.walkerholic.orderitem.service;

import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import com.yunhalee.walkerholic.orderitem.domain.OrderItemRepository;
import com.yunhalee.walkerholic.orderitem.exception.OrderItemNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderItemService {

    private OrderItemRepository orderItemRepository;

    public OrderItemService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public void update(Integer id, Integer qty) {
        OrderItem orderItem = findOrderItemById(id);
        orderItem.changeQty(qty);
    }

    public void deleteOrderItem(Integer id) {
        OrderItem orderItem = findOrderItemById(id);
        orderItemRepository.delete(orderItem);
    }

    public OrderItem findOrderItemById(Integer id){
        return orderItemRepository.findById(id)
            .orElseThrow(()->new OrderItemNotFoundException("OrderItem not found with id : " + id));
    }
}
