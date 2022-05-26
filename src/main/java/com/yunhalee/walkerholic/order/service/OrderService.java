package com.yunhalee.walkerholic.order.service;

import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.cart.service.CartService;
import com.yunhalee.walkerholic.common.notification.mapper.NotificationMapper;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.order.dto.OrderRequest;
import com.yunhalee.walkerholic.order.dto.OrderResponse;
import com.yunhalee.walkerholic.order.dto.OrderResponses;
import com.yunhalee.walkerholic.order.dto.SimpleOrderResponse;
import com.yunhalee.walkerholic.order.exception.NothingToPayException;
import com.yunhalee.walkerholic.order.exception.OrderDuplicated;
import com.yunhalee.walkerholic.order.exception.OrderNotFoundException;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import com.yunhalee.walkerholic.orderitem.service.OrderItemService;
import com.yunhalee.walkerholic.order.domain.OrderRepository;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.dto.UserIconResponse;
import com.yunhalee.walkerholic.user.service.UserService;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.dao.DataIntegrityViolationException;
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

    public OrderService(OrderRepository orderRepository,
        UserService userService,
        OrderItemService orderItemService,
        CartService cartService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.orderItemService = orderItemService;
        this.cartService = cartService;
    }

    public OrderResponse createOrder(OrderRequest request) {
        Cart cart = cartService.findCartByUserId(request.getUserId());
        checkCartEmpty(cart);
        User user = userService.findUserById(request.getUserId());
        Order order = saveOrder(request);
        Set<OrderItem> orderItems = saveOrderItems(cart, order);
        NotificationMapper.of(user.getNotificationType()).sendCreateOrderNotification(order, user);
        return OrderResponse.of(order,
            UserIconResponse.of(user),
            orderItemService.orderItemResponses(orderItems));
    }

    private Order saveOrder(OrderRequest request) {
        try {
            return orderRepository.save(request.toOrder());
        } catch (DataIntegrityViolationException e) {
            throw new OrderDuplicated("Order is duplicated. Please try a few seconds later.");
        }
    }

    private void checkCartEmpty(Cart cart) {
        if (cart.isEmpty()) {
            throw new NothingToPayException("Nothing to pay. Please add items.");
        }
    }


    private Set<OrderItem> saveOrderItems(Cart cart, Order order) {
        Set<OrderItem> orderItems = orderItemService.createOrderItems(cart.getCartItems(), order);
        cartService.emptyCart(cart);
        return orderItems;
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
        NotificationMapper.of(user.getNotificationType()).sendCancelOrderNotification(order, user);
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
        Pageable pageable = PageRequest.of(page - 1, ORDER_LIST_PER_PAGE);
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
