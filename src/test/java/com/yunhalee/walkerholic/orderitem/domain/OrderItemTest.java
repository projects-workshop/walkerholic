package com.yunhalee.walkerholic.orderitem.domain;

import com.yunhalee.walkerholic.product.domain.ProductTest;
import com.yunhalee.walkerholic.user.domain.UserTest;

public class OrderItemTest {

    public static final OrderItem ORDER_ITEM = new OrderItem(
        1,
        20,
        ProductTest.SECOND_PRODUCT);

}
