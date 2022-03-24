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

    @Column(name = "is_paid")
    private boolean isPaid = false;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    public PaymentInfo() {
        this.isPaid = false;
    }

    public PaymentInfo(boolean isPaid) {
        this.isPaid = isPaid;
    }

    @Builder
    public PaymentInfo(BigDecimal shipping, @NonNull boolean isPaid, String paymentMethod, LocalDateTime paidAt) {
        this.shipping = shipping;
        this.isPaid = isPaid;
        this.paymentMethod = paymentMethod;
        this.paidAt = paidAt;
    }

    public PaymentInfo(BigDecimal shipping, String paymentMethod) {
        this.shipping = shipping;
        this.paymentMethod = paymentMethod;
    }

    public void pay(BigDecimal shipping, String paymentMethod) {
        this.shipping = shipping;
        this.paymentMethod = paymentMethod;
        this.isPaid = true;
        this.paidAt = LocalDateTime.now();
    }

    public void checkOrderPaid() {
        if (!isPaid) {
            throw new OrderNotPaidException("Order must be paid.");
        }
    }

}
