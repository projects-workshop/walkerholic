package com.yunhalee.walkerholic.order.domain;

import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import com.yunhalee.walkerholic.order.domain.OrderStatus;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.orderitem.domain.OrderItemRepository;
import com.yunhalee.walkerholic.order.domain.OrderRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback(false)
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class OrderRepositoryTests {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    public static final int ORDER_LIST_PER_PAGE = 10;

    @Test
    public void createOrder() {
        //given
        User user = userRepository.findById(1).get();
        OrderItem orderItem = orderItemRepository.findById(1).get();
        Float shipping = 5.00f;
        String paymentMethod = "test";
        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(orderItem);

        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(orderItems);
        order.setOrderStatus(OrderStatus.ORDER);
        order.setShipping(shipping);
        order.setPaymentMethod(paymentMethod);

        //when
        Order order1 = orderRepository.save(order);

        //then
        assertThat(order1.getId()).isNotNull();
        assertThat(order1.getUser().getId()).isEqualTo(user.getId());
        assertThat(order1.getOrderItems().size()).isEqualTo(1);
        assertThat(order1.getShipping()).isEqualTo(shipping);
        assertThat(order1.getPaymentMethod()).isEqualTo(paymentMethod);
    }

    @Test
    public void updateOrder() {
        //given
        Integer orderId = 1;
        Order order = orderRepository.findById(orderId).get();
        Float originalShipping = order.getShipping();
        order.setShipping(originalShipping + 1f);

        //when
        Order order1 = orderRepository.save(order);

        //then
        assertThat(order1.getShipping()).isEqualTo(originalShipping + 1f);
    }

    @Test
    public void getByOrderId() {
        //given
        Integer orderId = 1;

        //when
        Order order = orderRepository.findById(orderId).get();

        //then
        assertThat(order.getId()).isEqualTo(orderId);
    }


    @Test
    public void getBySellerId() {
        //given
        Integer sellerId = 1;
        Integer page = 1;

        //when
        Pageable pageable = PageRequest.of(page - 1, ORDER_LIST_PER_PAGE);
        Page<Order> orderPage = orderRepository.findByUserId(pageable, sellerId, OrderStatus.CART);
        List<Order> orders = orderPage.getContent();

        //then
        for (Order order : orders) {
            List<Integer> orderItemSellerIds = order.getOrderItems().stream()
                .map(orderItem -> orderItem.getProduct().getUser().getId())
                .collect(Collectors.toList());
            assertThat(orderItemSellerIds).contains(sellerId);
        }

    }

    @Test
    public void getByUserId() {
        //given
        Integer userId = 1;
        Integer page = 1;

        //when
        Pageable pageable = PageRequest.of(page - 1, ORDER_LIST_PER_PAGE);
        Page<Order> orderPage = orderRepository.findByUserId(pageable, userId, OrderStatus.CART);
        List<Order> orders = orderPage.getContent();

        //then
        for (Order order : orders) {
            assertThat(userId).isEqualTo(order.getUser().getId());
        }

    }

    @Test
    public void getCartByUserId() {
        //given
        Integer userId = 1;

        //when
        Order order = orderRepository.findCartItemsByUserId(OrderStatus.CART, userId);

        //then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CART);
        assertThat(order.getUser().getId()).isEqualTo(userId);
    }

    @Test
    public void getOrders() {
        //given
        Integer page = 1;

        //when
        Pageable pageable = PageRequest.of(page - 1, ORDER_LIST_PER_PAGE);
        Page<Order> orderPage = orderRepository.findAll(pageable, OrderStatus.CART);
        List<Order> orders = orderPage.getContent();

        //then
        assertThat(orders.size()).isNotEqualTo(0);
    }

    @Test
    public void cancelOrder() {
        //given
        Integer orderId = 3;
        Order order = orderRepository.findById(orderId).get();
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItemRepository.deleteById(orderItem.getId());
        }

        //when
        orderRepository.deleteById(orderId);

        //then
        assertThat(orderRepository.findById(orderId)).isNull();
    }
}
