package com.yunhalee.walkerholic.order.domain;

import com.yunhalee.walkerholic.order.exception.OrderNotPaidException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Embeddable
@Getter
public class PaymentInfo {

    private BigDecimal shipping;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    public PaymentInfo(BigDecimal shipping, String paymentMethod) {
        this.shipping = shipping;
        this.paymentMethod = paymentMethod;
        this.paidAt = LocalDateTime.now();
    }
}
