package com.yunhalee.walkerholic.order.service;


import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.order.dto.CartResponse;
import com.yunhalee.walkerholic.order.dto.PayOrderRequest;
import com.yunhalee.walkerholic.order.dto.OrderResponse;
import com.yunhalee.walkerholic.order.dto.SimpleOrderResponse;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import com.yunhalee.walkerholic.order.domain.OrderStatus;
import com.yunhalee.walkerholic.orderitem.domain.OrderItemRepository;
import com.yunhalee.walkerholic.order.domain.OrderRepository;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemRequest;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemResponse;
import com.yunhalee.walkerholic.useractivity.dto.AddressDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTests {

    @Autowired
    OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Test
    public void createOrder() {
        //given
        AddressDTO addressDTO = new AddressDTO("testAddress", "testCountry", "testCity",
            "testZipcode", "testAddress");
        String paymentMethod = "testPaymentMethod";
        Integer orderItemId = 1;
        Integer userId = 1;
        List<OrderItemRequest> orderItems = new ArrayList<>();
        orderItems.add(new OrderItemRequest(orderItemRepository.findById(orderItemId).get()));
        PayOrderRequest orderCreateDTO = new PayOrderRequest(paymentMethod, addressDTO, orderItems,
            userId);

        //when
        OrderResponse orderDTO = orderService.createOrder(orderCreateDTO);

        //then
        assertNotNull(orderDTO.getId());
        assertEquals(orderDTO.getPaymentMethod(), paymentMethod);
        assertNotEquals(orderDTO.getOrderItems().size(), 0);
        assertNotNull(orderDTO.getUser());
        assertEquals(orderDTO.getAddress().getName(), "testAddress");
    }

    @Test
    public void createCart() {
        //given
        Integer userId = 1;

        //when
        Integer orderId = orderService.createCart(userId);

        //then
        assertNotNull(orderId);
        assertEquals(orderRepository.findById(orderId).get().getOrderStatus(), OrderStatus.CART);
        assertEquals(orderRepository.findById(orderId).get().getUser().getId(), userId);
    }

    @Test
    public void addToCart() {
        //given
        Integer orderId = 1;
        Integer qty = 2;
        Integer productId = 1;
        OrderItemRequest orderItemCreateDTO = new OrderItemRequest(qty, productId, orderId);

        //when
        OrderItemResponse orderItemDTO = orderService.addToCart(orderId, orderItemCreateDTO);

        //then
        assertNotNull(orderItemDTO.getId());
        assertEquals(orderItemDTO.getProductId(), productId);
        assertEquals(orderItemDTO.getQty(), qty);
        List<Integer> productIds = orderRepository.findById(orderId).get().getOrderItems().stream()
            .map(orderItem -> orderItem.getProduct().getId()).collect(Collectors.toList());
        assertThat(productIds).contains(productId);
    }

    @Test
    public void payOrder() {
        //given
        Integer orderId = 1;
        String paymentMethod = "testPaymentMethod";
        Float shipping = 2.00f;
        AddressDTO addressDTO = new AddressDTO("testAddress", "testCountry", "testCity",
            "testZipcode", "testAddress");

        PayOrderRequest orderCreateDTO = new PayOrderRequest(orderId, paymentMethod, shipping,
            addressDTO);

        //when
        orderService.payOrder(orderCreateDTO);
        Order order = orderRepository.findById(orderId).get();

        //then
        assertEquals(order.getOrderStatus(), OrderStatus.ORDER);
        assertNotNull(order.getPaidAt());
        assertTrue(order.isPaid());
        assertEquals(order.getShipping(), shipping);
        assertEquals(order.getPaymentMethod(), paymentMethod);
        assertEquals(order.getAddress().getName(), "testAddress");
    }

    @Test
    public void cancelOrder() {
        //given
        Integer orderId = 1;
        List<Integer> originalStock = orderRepository.findById(orderId).get().getOrderItems()
            .stream().map(orderItem -> orderItem.getProduct().getStock())
            .collect(Collectors.toList());
        List<Integer> qty = orderRepository.findById(orderId).get().getOrderItems().stream()
            .map(orderItem -> orderItem.getQty()).collect(Collectors.toList());

        //when
        SimpleOrderResponse orderListDTO = orderService.cancelOrder(orderId);

        //then
        assertEquals(orderListDTO.getOrderStatus(), OrderStatus.CANCEL.name());
        List<Integer> canceledStock = orderRepository.findById(orderId).get().getOrderItems()
            .stream().map(orderItem -> orderItem.getProduct().getStock())
            .collect(Collectors.toList());
        for (int i = 0; i < originalStock.size(); i++) {
            Set<OrderItem> orderItems = orderRepository.findById(orderId).get().getOrderItems();
            Integer recoveredStock = originalStock.get(i) + qty.get(i);
            assertEquals(recoveredStock, canceledStock.get(i));
        }
    }

    @Test
    public void deliverOrder() {
        //given
        Integer orderId = 1;

        //when
        SimpleOrderResponse orderListDTO = orderService.deliverOrder(orderId);

        //then
        assertTrue(orderListDTO.isDelivered());
        assertNotNull(orderListDTO.getDeliveredAt());
    }

    @Test
    public void getOrderById() {
        //given
        Integer orderId = 1;

        //when
        OrderResponse orderDTO = orderService.getOrder(orderId);

        //then
        assertEquals(orderDTO.getId(), orderId);
    }

    @Test
    public void getCartByUserId() {
        //given
        Integer userId = 1;

        //when
        CartResponse orderCartDTO = orderService.getCart(userId);

        //then
        assertNotNull(orderCartDTO.getId());
        assertEquals(orderRepository.findById(orderCartDTO.getId()).get().getUser().getId(),
            userId);
        assertEquals(orderRepository.findById(orderCartDTO.getId()).get().getOrderStatus(),
            OrderStatus.CART);
    }

    @Test
    public void getOrders() {
        //given
        Integer page = 1;

        //when
        HashMap<String, Object> response = orderService.getOrderList(page);
        List<SimpleOrderResponse> orderListDTOS = (List<SimpleOrderResponse>) response.get("orders");

        //then
        assertNotEquals(orderListDTOS.size(), 0);
    }

    @Test
    public void getOrdersBySellerId() {
        //given
        Integer sellerId = 1;
        Integer page = 1;

        //when
        HashMap<String, Object> response = orderService.getOrderListBySeller(page, sellerId);
        List<SimpleOrderResponse> orderListDTOS = (List<SimpleOrderResponse>) response.get("orders");

        //then
        for (SimpleOrderResponse orderListDTO : orderListDTOS) {
            List<Integer> sellerIds = orderRepository.findById(orderListDTO.getId()).get()
                .getOrderItems().stream().map(orderItem -> orderItem.getProduct().getUser().getId())
                .collect(Collectors.toList());
            assertThat(sellerIds).contains(sellerId);
        }
    }

    @Test
    public void getOrdersByUserId() {
        //given
        Integer userId = 1;
        Integer page = 1;

        //when
        HashMap<String, Object> response = orderService.getOrderListByUser(page, userId);
        List<SimpleOrderResponse> orderListDTOS = (List<SimpleOrderResponse>) response.get("orders");

        //then
        for (SimpleOrderResponse orderListDTO : orderListDTOS) {
            assertEquals(orderRepository.findById(orderListDTO.getId()).get().getUser().getId(),
                userId);
        }
    }


}
