package com.yunhalee.walkerholic.orderitem.service;

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
    private ProductService productService;
    private OrderRepository orderRepository;

    public OrderItemService(
        OrderItemRepository orderItemRepository,
        ProductService productService,
        OrderRepository orderRepository) {
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
        this.orderRepository = orderRepository;
    }

    public OrderItemResponse create(Integer id, OrderItemRequest request){
        Order order = orderRepository.findById(id).orElseThrow(()-> new OrderNotFoundException(""));
        Product product = productService.findProductById(request.getProductId());
        OrderItem orderItem = OrderItem.of(request.getQty(), product, order);
        orderItemRepository.save(orderItem);
        return OrderItemResponse.of(orderItem);
    }

    public OrderItemResponse update(Integer id, Integer qty) {
        OrderItem orderItem = findOrderItemById(id);
        orderItem.changeQty(qty);
        return OrderItemResponse.of(orderItem);
    }

    public void deleteOrderItem(Integer id) {
        OrderItem orderItem = findOrderItemById(id);
        orderItemRepository.delete(orderItem);
    }

    public OrderItem findOrderItemById(Integer id) {
        return orderItemRepository.findById(id)
            .orElseThrow(
                () -> new OrderItemNotFoundException("OrderItem not found with id : " + id));
    }

    public OrderItemResponses orderItemResponses(Set<OrderItem> orderItems) {
        return OrderItemResponses.of(
            orderItems.stream()
                .map(OrderItemResponse::of)
                .collect(Collectors.toList()));
    }
}
