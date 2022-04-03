package com.yunhalee.walkerholic.order.domain;

import com.yunhalee.walkerholic.user.domain.UserTest;

public class OrderTest {

    public static final Order ORDER =Order.builder()
        .id(2)
        .orderStatus(OrderStatus.ORDER)
        .paymentInfo(PaymentInfoTest.PAYMENT_INFO)
        .deliveryInfo(DeliveryInfoTest.DELIVERED_DELIVERY_INFO)
        .userId(UserTest.USER.getId()).build();

    public static final Order CANCEL = Order.builder()
        .id(3)
        .orderStatus(OrderStatus.CANCEL)
        .paymentInfo(PaymentInfoTest.PAYMENT_INFO)
        .deliveryInfo(DeliveryInfoTest.DELIVERED_DELIVERY_INFO)
        .userId(UserTest.USER.getId()).build();
}
