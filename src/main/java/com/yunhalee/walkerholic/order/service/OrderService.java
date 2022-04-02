package com.yunhalee.walkerholic.order.service;

import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.cart.service.CartService;
import com.yunhalee.walkerholic.common.service.NotificationService;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.order.dto.OrderRequest;
import com.yunhalee.walkerholic.order.dto.OrderResponse;
import com.yunhalee.walkerholic.order.dto.OrderResponses;
import com.yunhalee.walkerholic.order.dto.SimpleOrderResponse;
import com.yunhalee.walkerholic.order.exception.OrderNotFoundException;
import com.yunhalee.walkerholic.orderitem.service.OrderItemService;
import com.yunhalee.walkerholic.order.domain.OrderRepository;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.dto.UserIconResponse;
import com.yunhalee.walkerholic.user.service.UserService;
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
    private CartService cartService;
    private NotificationService notificationService;

    public OrderService(OrderRepository orderRepository, UserService userService,
        OrderItemService orderItemService, CartService cartService,
        NotificationService notificationService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.orderItemService = orderItemService;
        this.cartService = cartService;
        this.notificationService = notificationService;
    }


    public OrderResponse createOrder(OrderRequest request) {
        Cart cart = cartService.findCartByUserId(request.getUserId());
        User user = userService.findUserById(request.getUserId());
        Order order = orderRepository.save(request.toOrder(cart.getCartItems()));
        notificationService.sendCreateOrderNotification(order, user);
        return OrderResponse.of(order,
            UserIconResponse.of(user),
            orderItemService.orderItemResponses(order.getOrderItems()));
    }

    public SimpleOrderResponse deliverOrder(Integer id) {
        Order order = findOrderById(id);
        User user = userService.findUserById(order.getUserId());
        order.deliver();
        return SimpleOrderResponse.of(order, UserIconResponse.of(user));
    }

    public SimpleOrderResponse cancelOrder(Integer id) {
        Order order = findOrderById(id);
        User user = userService.findUserById(order.getUserId());
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
    public OrderResponses getOrderList(Integer page) {
        Pageable pageable = PageRequest.of(page - 1, ORDER_LIST_PER_PAGE);
        Page<Order> orderPage = orderRepository.findAllOrders(pageable);
        return orderResponses(orderPage);
    }

    @Transactional(readOnly = true)
    public OrderResponses getOrderListBySeller(Integer page, Integer sellerId) {
        Pageable pageable = PageRequest.of(page -1, ORDER_LIST_PER_PAGE);
        Page<Order> orderPage = orderRepository.findBySellerId(pageable, sellerId);
        return orderResponses(orderPage);
    }

    @Transactional(readOnly = true)
    public OrderResponses getOrderListByUser(Integer page, Integer userId) {
        Pageable pageable = PageRequest.of(page - 1, ORDER_LIST_PER_PAGE);
        Page<Order> orderPage = orderRepository.findByUserId(pageable, userId);
        return orderResponses(orderPage);
    }


    public Order findOrderById(Integer id) {
        return orderRepository.findById(id)
            .orElseThrow(() -> new OrderNotFoundException("Order not found with id : " + id));
    }

    private OrderResponse orderResponse(Order order) {
        return OrderResponse.of(order,
            UserIconResponse.of(userService.findUserById(order.getUserId())),
            orderItemService.orderItemResponses(order.getOrderItems()));
    }

    private OrderResponses orderResponses(Page<Order> orderPage) {
        return OrderResponses.of(
            orderPage.getContent().stream()
                .map(order -> SimpleOrderResponse
                    .of(order, UserIconResponse
                        .of(userService.findUserById(order.getUserId()))))
                .collect(Collectors.toList()),
            orderPage.getTotalElements(),
            orderPage.getTotalPages());
    }

}
