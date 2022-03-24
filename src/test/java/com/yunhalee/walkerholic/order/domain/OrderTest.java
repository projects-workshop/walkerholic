package com.yunhalee.walkerholic.order.domain;

import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserTest;

public class OrderTest {

    public static final Order CART = new Order(
        1,
        OrderStatus.CART,
        PaymentInfoTest.NOT_PAID_PAYMENT_INFO,
        DeliveryInfoTest.NOT_DELIVERED_DELIVERY_INFO,
        UserTest.USER);

    public static final Order ORDER = new Order(
        2,
        OrderStatus.ORDER,
        PaymentInfoTest.PAID_PAYMENT_INFO,
        DeliveryInfoTest.DELIVERED_DELIVERY_INFO,
        UserTest.USER);

    public static final Order CANCEL = new Order(
        3,
        OrderStatus.CANCEL,
        PaymentInfoTest.PAID_PAYMENT_INFO,
        DeliveryInfoTest.DELIVERED_DELIVERY_INFO,
        UserTest.USER);
}
