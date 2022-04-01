package com.yunhalee.walkerholic.order.service;

import com.yunhalee.walkerholic.common.service.NotificationService;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.order.dto.CartResponse;
import com.yunhalee.walkerholic.order.dto.OrderRequest;
import com.yunhalee.walkerholic.order.dto.PayOrderRequest;
import com.yunhalee.walkerholic.order.dto.OrderResponse;
import com.yunhalee.walkerholic.order.dto.OrderResponses;
import com.yunhalee.walkerholic.order.dto.SimpleOrderResponse;
import com.yunhalee.walkerholic.order.exception.OrderNotFoundException;
import com.yunhalee.walkerholic.order.domain.OrderStatus;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemResponses;
import com.yunhalee.walkerholic.orderitem.service.OrderItemService;
import com.yunhalee.walkerholic.order.domain.OrderRepository;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.dto.UserIconResponse;
import com.yunhalee.walkerholic.user.service.UserService;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OrderService {

    public static final int ORDER_LIST_PER_PAGE = 10;

    private OrderRepository orderRepository;
    private UserService userService;
    private OrderItemService orderItemService;
    private NotificationService notificationService;

    public OrderService(OrderRepository orderRepository, UserService userService, OrderItemService orderItemService, NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.orderItemService = orderItemService;
        this.notificationService = notificationService;
    }

    public OrderResponse createOrder(Integer id, OrderRequest request) {
        User user = userService.findUserById(id);
        Order order = orderRepository.save(Order.createCart(user));
        OrderItemResponses orderItems = orderItemService.createOrderItems(order, request.getOrderItems());
        order.pay(request.toOrder());
        notificationService.sendCreateOrderNotification(order, user);
        return OrderResponse.of(order,
            UserIconResponse.of(user),
            orderItems);
    }

    public Integer createCart(Integer id) {
        User user = userService.findUserById(id);
        Order order = orderRepository.save(Order.createCart(user));
        return order.getId();
    }

    public void payOrder(Integer id, PayOrderRequest request) {
        Order order = findOrderById(id);
        order.pay(request.toOrder());
        notificationService.sendCreateOrderNotification(order, order.getUser());
    }

    public SimpleOrderResponse deliverOrder(Integer id) {
        Order order = findOrderById(id);
        User user = order.getUser();
        order.deliver();
        return SimpleOrderResponse.of(order, UserIconResponse.of(user));
    }

    public SimpleOrderResponse cancelOrder(Integer id) {
        Order order = findOrderById(id);
        User user = order.getUser();
        order.cancel();
        notificationService.sendCancelOrderNotification(order, user);
        return SimpleOrderResponse.of(order, UserIconResponse.of(user));
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(Integer id) {
        Order order = orderRepository.findByOrderId(id);
        return orderResponse(order);
    }

    @Transactional(readOnly = true)
    public CartResponse getCart(Integer id) {
        Optional<Order> order = orderRepository.findCartItemsByUserId(OrderStatus.CART, id);
        if (order.isPresent()) {
            Order cart = order.get();
            return new CartResponse(cart,
                orderItemService.orderItemResponses(cart.getOrderItems()));
        }
        return new CartResponse();
    }


    @Transactional(readOnly = true)
    public OrderResponses getOrderList(Integer page) {
        Pageable pageable = PageRequest.of(page - 1, ORDER_LIST_PER_PAGE);
        Page<Order> orderPage = orderRepository.findAllOrders(pageable, OrderStatus.CART);
        return orderResponses(orderPage);
    }

    @Transactional(readOnly = true)
    public OrderResponses getOrderListBySeller(Integer page, Integer id) {
        Pageable pageable = PageRequest.of(page - 1, ORDER_LIST_PER_PAGE);
        Page<Order> orderPage = orderRepository.findBySellerId(pageable, id, OrderStatus.CART);
        return orderResponses(orderPage);
    }

    @Transactional(readOnly = true)
    public OrderResponses getOrderListByUser(Integer page, Integer id) {
        Pageable pageable = PageRequest.of(page - 1, ORDER_LIST_PER_PAGE);
        Page<Order> orderPage = orderRepository.findByUserId(pageable, id, OrderStatus.CART);
        return orderResponses(orderPage);
    }


    public Order findOrderById(Integer id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException("Order not found with id : " + id));
    }

    private OrderResponse orderResponse(Order order) {
        return OrderResponse.of(order,
            UserIconResponse.of(order.getUser()),
            orderItemService.orderItemResponses(order.getOrderItems()));
    }

    private OrderResponses orderResponses(Page<Order> orderPage) {
        return OrderResponses.of(
            orderPage.getContent().stream()
                .map(order -> SimpleOrderResponse.of(order, UserIconResponse.of(order.getUser())))
                .collect(Collectors.toList()),
            orderPage.getTotalElements(),
            orderPage.getTotalPages());
    }

}
