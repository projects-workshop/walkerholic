package com.yunhalee.walkerholic.order.service;


import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.cart.domain.Cart;
import com.yunhalee.walkerholic.cartItem.domain.CartItemTest;
import com.yunhalee.walkerholic.common.dto.ItemResponse;
import com.yunhalee.walkerholic.order.domain.Address;
import com.yunhalee.walkerholic.order.domain.AddressTest;
import com.yunhalee.walkerholic.order.domain.DeliveryInfoTest;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.order.domain.OrderItems;
import com.yunhalee.walkerholic.order.domain.OrderStatus;
import com.yunhalee.walkerholic.order.domain.PaymentInfoTest;
import com.yunhalee.walkerholic.order.dto.AddressResponse;
import com.yunhalee.walkerholic.order.dto.OrderRequest;
import com.yunhalee.walkerholic.order.dto.OrderResponse;
import com.yunhalee.walkerholic.order.dto.SimpleOrderResponse;
import com.yunhalee.walkerholic.order.exception.NothingToPayException;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemRequest;
import com.yunhalee.walkerholic.product.domain.Category;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.productImage.domain.ProductImageTest;
import com.yunhalee.walkerholic.user.domain.UserTest;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;

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

    @InjectMocks
    OrderService orderService = new OrderService(
        orderRepository,
        userService,
        orderItemService,
        cartService,
        notificationService
    );

    private OrderItem orderItem;
    private Product product;
    private Order order;
    private Cart cart;


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
        order = Order.builder()
            .id(1)
            .orderStatus(OrderStatus.ORDER)
            .payment(PaymentInfoTest.PAYMENT_INFO)
            .delivery(DeliveryInfoTest.NOT_DELIVERED_DELIVERY)
            .userId(UserTest.USER.getId())
            .orderItems(new OrderItems()).build();
        orderItem = new OrderItem(
            1,
            20,
            product,
            order);
        cart = new Cart(1, UserTest.USER.getId());
        cart.addCartItem(CartItemTest.FIRST_CART_ITEM);
        order.addOrderItem(orderItem);
    }


    @Test
    public void create_order() {
        //given
        OrderRequest request = new OrderRequest(
            UserTest.USER.getId(),
            SHIPPING,
            PAYMENT_METHOD,
            new AddressResponse(ADDRESS),
            Arrays.asList(new OrderItemRequest(2, product.getId())));

        //when
        when(cartService.findCartByUserId(anyInt())).thenReturn(cart);
        when(userService.findUserById(anyInt())).thenReturn(UserTest.USER);
        when(orderRepository.save(any())).thenReturn(order);
        when(orderItemService.orderItemResponses(any())).thenReturn(Arrays.asList(ItemResponse.of(orderItem)));
        OrderResponse orderResponse = orderService.createOrder(request);

        //then
        verify(notificationService).sendCreateOrderNotification(any(), any());
        isEqual(orderResponse.getItems().get(0), orderItem);
    }

    @Test
    public void create_order_with_empty_cart_is_invalid() {
        Cart emptyCart = new Cart(2, UserTest.SELLER.getId());
        OrderRequest request = new OrderRequest(
            UserTest.SELLER.getId(),
            SHIPPING,
            PAYMENT_METHOD,
            new AddressResponse(ADDRESS),
            Arrays.asList(new OrderItemRequest(2, product.getId())));

        when(cartService.findCartByUserId(anyInt())).thenReturn(emptyCart);
        assertThatThrownBy(() -> orderService.createOrder(request))
            .isInstanceOf(NothingToPayException.class)
            .hasMessage(NOTHING_TO_PAY_EXCEPTION);
    }

    @Test
    public void deliver_order() {
        //when
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));
        when(userService.findUserById(anyInt())).thenReturn(UserTest.USER);
        SimpleOrderResponse response = orderService.deliverOrder(order.getId());

        //then
        assertThat(response.isDelivered()).isTrue();
        assertThat(response.getDeliveredAt()).isNotNull();
    }

    @Test
    public void cancel_order() {
        //when
        when(userService.findUserById(anyInt())).thenReturn(UserTest.USER);
        when(orderRepository.findById(anyInt())).thenReturn(Optional.of(order));
        SimpleOrderResponse response = orderService.cancelOrder(order.getId());

        //then
        verify(notificationService).sendCancelOrderNotification(any(), any());
        assertThat(product.getStock()).isEqualTo(52);
        assertThat(response.getOrderStatus()).isEqualTo(OrderStatus.CANCEL.name());

    }

    @Test
    public void find_order() {
        // when
        when(orderRepository.findByOrderId(anyInt())).thenReturn(order);
        when(userService.findUserById(anyInt())).thenReturn(UserTest.USER);
        when(orderItemService.orderItemResponses(any())).thenReturn(Arrays.asList(ItemResponse.of(orderItem)));
        OrderResponse response = orderService.getOrder(order.getId());

        // then
        isEqual(order, response);
    }


    private void isEqual(Order order, OrderResponse response) {
        assertThat(response.getId()).isEqualTo(order.getId());
        assertThat(response.getOrderStatus()).isEqualTo(order.getOrderStatus().name());
        assertThat(response.getPaymentMethod()).isEqualTo(order.getPaymentMethod());
        assertThat(response.getPaidAt()).isEqualTo(order.getPaidAt());
        assertThat(response.isDelivered()).isEqualTo(order.isDelivered());
        assertThat(response.getDeliveredAt()).isEqualTo(order.getDeliveredAt());
        assertThat(response.getUser().getId()).isEqualTo(order.getUserId());
        assertThat(response.getItems().size()).isEqualTo(order.getOrderItems().size());
        assertThat(response.getTotal()).isEqualTo(order.getTotalAmount());
        assertThat(response.getShipping()).isEqualTo(order.getShipping());
    }

    private void isEqual(ItemResponse orderItemResponse, OrderItem orderItem) {
        assertThat(orderItemResponse.getId()).isEqualTo(orderItem.getId());
        assertThat(orderItemResponse.getProductId()).isEqualTo(orderItem.getProduct().getId());
    }

}
