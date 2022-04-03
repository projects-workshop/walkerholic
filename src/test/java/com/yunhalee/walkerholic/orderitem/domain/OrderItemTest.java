package com.yunhalee.walkerholic.orderitem.domain;

import com.yunhalee.walkerholic.order.domain.OrderTest;
import com.yunhalee.walkerholic.product.domain.ProductTest;
import com.yunhalee.walkerholic.user.domain.UserTest;

public class OrderItemTest {

    public static final OrderItem FIRST_ORDER_ITEM = new OrderItem(
        1,
        20,
        ProductTest.SECOND_PRODUCT,
        OrderTest.ORDER);

    public static final OrderItem SECOND_ORDER_ITEM = new OrderItem(
        2,
        3,
        ProductTest.THIRD_PRODUCT,
        OrderTest.ORDER);

}
