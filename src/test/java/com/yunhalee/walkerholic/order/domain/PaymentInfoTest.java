package com.yunhalee.walkerholic.order.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentInfoTest {

    public static final PaymentInfo NOT_PAID_PAYMENT_INFO = PaymentInfo.builder()
        .isPaid(false).build();

    public static final PaymentInfo PAID_PAYMENT_INFO = PaymentInfo.builder()
        .shipping(BigDecimal.TEN)
        .isPaid(true)
        .paymentMethod("testPaymentMethod")
        .paidAt(LocalDateTime.now()).build();
}
