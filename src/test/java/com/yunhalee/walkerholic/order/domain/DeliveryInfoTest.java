package com.yunhalee.walkerholic.order.domain;

import java.time.LocalDateTime;

public class DeliveryInfoTest {

    public static final DeliveryInfo NOT_DELIVERED_DELIVERY_INFO = DeliveryInfo.builder()
        .address(AddressTest.ADDRESS).build();

    public static final DeliveryInfo DELIVERED_DELIVERY_INFO = DeliveryInfo.builder()
        .isDelivered(true)
        .deliveredAt(LocalDateTime.now())
        .address(AddressTest.ADDRESS).build();
}
