package com.yunhalee.walkerholic.order.domain;

import com.yunhalee.walkerholic.order.exception.OrderAlreadyDeliveredException;
import com.yunhalee.walkerholic.orderitem.domain.OrderItemTest;
import com.yunhalee.walkerholic.user.domain.UserTest;
import java.math.BigDecimal;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class OrderTest {

    private static final String ORDER_ALREADY_DELIVERED_EXCEPTION = "Order Already Completed. All the items has been delivered.";

    public static final Order ORDER = Order.builder()
        .id(2)
        .orderStatus(OrderStatus.ORDER)
        .payment(PaymentInfoTest.PAYMENT_INFO)
        .delivery(DeliveryInfoTest.DELIVERED_DELIVERY)
        .userId(UserTest.USER.getId())
        .orderItems(new OrderItems()).build();

    public static final Order CANCEL = Order.builder()
        .id(3)
        .orderStatus(OrderStatus.CANCEL)
        .payment(PaymentInfoTest.PAYMENT_INFO)
        .delivery(DeliveryInfoTest.DELIVERED_DELIVERY)
        .userId(UserTest.USER.getId())
        .orderItems(new OrderItems()).build();

    private Order order;

    void setUp() {
        order = Order.of(UserTest.USER.getId(),
            BigDecimal.TEN,
            PaymentInfoTest.PAYMENT_INFO.getTransactionId(),
            PaymentInfoTest.PAYMENT_INFO.getPaymentMethod(),
            AddressTest.ADDRESS);
        order.addOrderItem(OrderItemTest.FIRST_ORDER_ITEM);
    }

    @Test
    public void deliver_order() {
        // given
        setUp();

        // when
        order.deliver();

        // then
        assertThat(order.isDelivered()).isTrue();
        assertThat(order.getDeliveredAt()).isNotNull();
    }

    @Test
    public void cancel_order() {
        // given
        setUp();

        // when
        order.cancel();

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CANCEL);
    }

    @Test
    public void cancel_already_delivered_order_is_invalid() {
        // given
        setUp();
        order.deliver();

        // when
        assertThatThrownBy(() -> order.cancel())
            .isInstanceOf(OrderAlreadyDeliveredException.class)
            .hasMessage(ORDER_ALREADY_DELIVERED_EXCEPTION);
    }

}
