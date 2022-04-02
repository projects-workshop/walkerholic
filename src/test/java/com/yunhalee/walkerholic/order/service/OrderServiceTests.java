package com.yunhalee.walkerholic.order.service;


import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.order.domain.Address;
import com.yunhalee.walkerholic.order.domain.AddressTest;
import com.yunhalee.walkerholic.order.domain.DeliveryInfoTest;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.order.domain.OrderStatus;
import com.yunhalee.walkerholic.order.domain.PaymentInfoTest;
import com.yunhalee.walkerholic.order.dto.AddressResponse;
import com.yunhalee.walkerholic.cart.dto.CartResponse;
import com.yunhalee.walkerholic.order.dto.OrderRequest;
import com.yunhalee.walkerholic.order.dto.OrderResponse;
import com.yunhalee.walkerholic.order.dto.OrderResponses;
import com.yunhalee.walkerholic.order.dto.PayOrderRequest;
import com.yunhalee.walkerholic.order.dto.SimpleOrderResponse;
import com.yunhalee.walkerholic.order.exception.NothingToPayException;
import com.yunhalee.walkerholic.order.exception.OrderAlreadyDeliveredException;
import com.yunhalee.walkerholic.order.exception.OrderNotPaidException;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemRequest;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemResponse;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemResponses;
import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.productImage.domain.ProductImageTest;
import com.yunhalee.walkerholic.user.domain.UserTest;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyInt;

class OrderServiceTests extends MockBeans {

    private static final Address ADDRESS = AddressTest.ADDRESS;
    private static final Float SHIPPING = 10.0f;
    private static final String PAYMENT_METHOD = "testPaymentMethod";

    private static final String NOTHING_TO_PAY_EXCEPTION = "Nothing to pay. Please add items.";
    private static final String ORDER_NOT_PAID_EXCEPTION = "Order must be paid.";
    private static final String ORDER_ALREADY_DELIVERED_EXCEPTION = "Order Already Completed. All the items has been delivered.";

    @InjectMocks
    OrderService orderService = new OrderService(
        orderRepository,
        userService,
        orderItemService,
        notificationService
    );

    private OrderItem orderItem;
    private Product product;
    private Order cart;
    private Order order;


    @BeforeEach
    public void setUp() {
        product = new Product(
            2,
            "secondProduct",
            "testDescription",
            "testBrand",
            Category.BAG,
            32,
            21.0f,
            UserTest.SELLER,
            ProductImageTest.PRODUCT_IMAGE);
        orderItem = new OrderItem(
            1,
            20,
            product);
        order = new Order(
            1,
            OrderStatus.ORDER,
            PaymentInfoTest.PAID_PAYMENT_INFO,
            DeliveryInfoTest.NOT_DELIVERED_DELIVERY_INFO,
            UserTest.USER
        );
        order.addOrderItem(orderItem);

        cart = new Order(
            2,
            OrderStatus.CART,
            PaymentInfoTest.NOT_PAID_PAYMENT_INFO,
            DeliveryInfoTest.NOT_DELIVERED_DELIVERY_INFO,
            UserTest.USER
        );
    }


    @Test
    public void createOrder() {
        //given
        cart.addOrderItem(orderItem);
        OrderRequest request = new OrderRequest(
            SHIPPING,
            PAYMENT_METHOD,
            new AddressResponse(ADDRESS),
            Arrays.asList(new OrderItemRequest(20, product.getId())));

        //when
        when(userService.findUserById(anyInt())).thenReturn(UserTest.USER);
        when(orderRepository.save(any())).thenReturn(cart);
        when(orderItemService.createOrderItems(any(), any())).thenReturn(OrderItemResponses.of(Arrays.asList(OrderItemResponse.of(orderItem))));
        orderService.createOrder(UserTest.USER.getId(), request);

        //then
        verify(notificationService).sendCreateOrderNotification(any(), any());
        checkPay(cart, ADDRESS, SHIPPING, PAYMENT_METHOD);
    }

//    @Test
//    public void create_cart() {
//        //when
//        when(userService.findUserById(anyInt())).thenReturn(UserTest.USER);
//        when(orderRepository.save(any())).thenReturn(cart);
//        Integer orderId = orderService.createCart(UserTest.USER.getId());
//
//        //then
//        assertThat(orderId).isEqualTo(cart.getId());
//    }


    @Test
    public void pay_order() {
        //given
        cart.addOrderItem(orderItem);
        PayOrderRequest request = new PayOrderRequest(
            PAYMENT_METHOD,
            SHIPPING,
            new AddressResponse(ADDRESS));

        //when
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(cart));
        orderService.payOrder(cart.getId(), request);

        //then
        verify(notificationService).sendCreateOrderNotification(any(), any());
        assertThat(product.getStock()).isEqualTo(12);
        checkPay(cart, ADDRESS, SHIPPING, PAYMENT_METHOD);
    }

    @Test
    public void pay_without_items_is_invalid() {
        PayOrderRequest request = new PayOrderRequest(
            PAYMENT_METHOD,
            SHIPPING,
            new AddressResponse(ADDRESS));

        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(cart));
        assertThatThrownBy(() -> orderService.payOrder(cart.getId(), request))
            .isInstanceOf(NothingToPayException.class)
            .hasMessage(NOTHING_TO_PAY_EXCEPTION);
    }

    @Test
    public void deliver_order() {
        //when
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));
        SimpleOrderResponse response = orderService.deliverOrder(order.getId());

        //then
        assertThat(response.isDelivered()).isTrue();
        assertThat(response.getDeliveredAt()).isNotNull();
    }

    @Test
    public void deliver_without_pay_is_invalid() {
        cart.addOrderItem(orderItem);
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(cart));

        assertThatThrownBy(() -> orderService.deliverOrder(cart.getId()))
            .isInstanceOf(OrderNotPaidException.class)
            .hasMessage(ORDER_NOT_PAID_EXCEPTION);
    }


    @Test
    public void cancel_order() {
        //when
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));
        SimpleOrderResponse response = orderService.cancelOrder(order.getId());

        //then
        verify(notificationService).sendCancelOrderNotification(any(), any());
        assertThat(product.getStock()).isEqualTo(52);
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.CANCEL.name());

    }


    @Test
    public void cancel_with_already_delivered_order_is_invalid() {
        Order deliveredOrder = new Order(
            1,
            OrderStatus.ORDER,
            PaymentInfoTest.PAID_PAYMENT_INFO,
            DeliveryInfoTest.DELIVERED_DELIVERY_INFO,
            UserTest.USER
        );
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(deliveredOrder));

        assertThatThrownBy(() -> orderService.cancelOrder(deliveredOrder.getId()))
            .isInstanceOf(OrderAlreadyDeliveredException.class)
            .hasMessage(ORDER_ALREADY_DELIVERED_EXCEPTION);

    }


    @Test
    public void find_order() {
        //then
        when(orderRepository.findByOrderId(anyInt())).thenReturn(order);
        when(orderItemService.orderItemResponses(any())).thenReturn(OrderItemResponses.of(Arrays.asList(OrderItemResponse.of(orderItem))));
        OrderResponse response = orderService.getOrder(order.getId());

        //then
        isEqual(order, response);
    }

//    @Test
//    public void find_cart() {
//        //given
//        cart.addOrderItem(orderItem);
//
//        //when
//        when(orderRepository.findCartItemsByUserId(any(), anyInt())).thenReturn(Optional.of(cart));
//        when(orderItemService.orderItemResponses(any())).thenReturn(OrderItemResponses.of(Arrays.asList(OrderItemResponse.of(orderItem))));
//        CartResponse response = orderService.getCart(cart.getId());
//
//        //then
//        assertThat(response.getId()).isEqualTo(cart.getId());
//        assertThat(response.getOrderItems().getOrderItems().size()).isEqualTo(1);
//    }

    @Test
    public void find_orders() {
        //when
        when(orderRepository.findAllOrders(PageRequest.of(0, 10), OrderStatus.CART)).thenReturn(new PageImpl<>(Arrays.asList(order)));
        OrderResponses responses = orderService.getOrderList(1);

        //then
        assertThat(responses.getOrders().size()).isEqualTo(1);
        isEqual(order, responses.getOrders().get(0));
    }


    private void isEqual(Order order, OrderResponse response) {
        assertThat(response.getId()).isEqualTo(order.getId());
        assertThat(response.getOrderStatus()).isEqualTo(order.getOrderStatus().name());
        assertThat(response.isPaid()).isEqualTo(order.isPaid());
        assertThat(response.getPaymentMethod()).isEqualTo(order.getPaymentMethod());
        assertThat(response.getPaidAt()).isEqualTo(order.getPaidAt());
        assertThat(response.isDelivered()).isEqualTo(order.isDelivered());
        assertThat(response.getDeliveredAt()).isEqualTo(order.getDeliveredAt());
        assertThat(response.getUser().getId()).isEqualTo(order.getUser().getId());
        assertThat(response.getOrderItems().getOrderItems().size()).isEqualTo(order.getOrderItems().size());
        assertThat(response.getTotal()).isEqualTo(order.getTotalAmount());
        assertThat(response.getShipping()).isEqualTo(order.getShipping());
    }

    private void isEqual(Order order, SimpleOrderResponse response) {
        assertThat(response.getId()).isEqualTo(order.getId());
        assertThat(response.getOrderStatus()).isEqualTo(order.getOrderStatus().name());
        assertThat(response.isPaid()).isEqualTo(order.isPaid());
        assertThat(response.getPaidAt()).isEqualTo(order.getPaidAt());
        assertThat(response.isDelivered()).isEqualTo(order.isDelivered());
        assertThat(response.getDeliveredAt()).isEqualTo(order.getDeliveredAt());
        assertThat(response.getUser().getId()).isEqualTo(order.getUser().getId());
        assertThat(response.getTotalAmount()).isEqualTo(order.getTotalAmount());
    }

    private void checkPay(Order order, Address address, Float shipping, String paymentMethod) {
        assertThat(order.getDeliveryInfo().getAddress()).isEqualTo(address);
        assertThat(order.getShipping()).isEqualTo(BigDecimal.valueOf(shipping));
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.ORDER);
        assertThat(order.getPaymentMethod()).isEqualTo(paymentMethod);
        assertThat(order.isPaid()).isEqualTo(true);
        assertThat(order.getPaidAt()).isNotNull();
    }

}
