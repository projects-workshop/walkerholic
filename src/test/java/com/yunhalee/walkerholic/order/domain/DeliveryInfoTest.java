package com.yunhalee.walkerholic.order.domain;

import java.time.LocalDateTime;

public class DeliveryInfoTest {

    public static final Delivery NOT_DELIVERED_DELIVERY = Delivery.builder()
        .address(AddressTest.ADDRESS).build();

    public static final Delivery DELIVERED_DELIVERY = Delivery.builder()
        .isDelivered(true)
        .deliveredAt(LocalDateTime.now())
        .address(AddressTest.ADDRESS).build();
}
