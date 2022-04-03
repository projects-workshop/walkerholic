package com.yunhalee.walkerholic.orderitem.service;

import com.yunhalee.walkerholic.cartItem.domain.CartItem;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.order.domain.OrderRepository;
import com.yunhalee.walkerholic.order.exception.OrderNotFoundException;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import com.yunhalee.walkerholic.orderitem.domain.OrderItemRepository;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemRequest;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemResponse;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemResponses;
import com.yunhalee.walkerholic.orderitem.exception.OrderItemNotFoundException;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.product.service.ProductService;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
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

    public void deleteOrderItem(Integer id) {
        OrderItem orderItem = findOrderItemById(id);
        orderItemRepository.delete(orderItem);
    }

    public Set<OrderItem> createOrderItems(Set<CartItem> cartItems, Order order) {
        return cartItems.stream()
            .map(cartItem -> orderItemRepository.save(OrderItem.of(cartItem.getQty(), cartItem.getProduct(), order)))
            .collect(Collectors.toSet());
    }

    public OrderItem findOrderItemById(Integer id) {
        return orderItemRepository.findById(id)
            .orElseThrow(() -> new OrderItemNotFoundException("OrderItem not found with id : " + id));
    }

    public OrderItemResponses orderItemResponses(Set<OrderItem> orderItems) {
        return OrderItemResponses.of(
            orderItems.stream()
                .map(OrderItemResponse::of)
                .collect(Collectors.toList()));
    }
}
