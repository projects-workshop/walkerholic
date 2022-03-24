package com.yunhalee.walkerholic.order.domain;

import com.yunhalee.walkerholic.order.exception.OrderAlreadyDeliveredException;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Embeddable
@Getter
public class DeliveryInfo {

    @Column(name = "is_delivered")
    private boolean isDelivered = false;

    @Column(name = "delivered_at")
    private LocalDateTime deliveredAt;

    @Embedded
    private Address address;

    public DeliveryInfo() {
        this.isDelivered = false;
    }

    public DeliveryInfo(Address address) {
        this.address = address;
    }

    @Builder
    public DeliveryInfo(boolean isDelivered, LocalDateTime deliveredAt, @NonNull Address address) {
        this.isDelivered = isDelivered;
        this.deliveredAt = deliveredAt;
        this.address = address;
    }

    public void deliver() {
        this.isDelivered = true;
        this.deliveredAt = LocalDateTime.now();
    }

    public void changeAddress(DeliveryInfo deliveryInfo) {
        this.address = deliveryInfo.getAddress();
    }

    public void checkAlreadyDelivered() {
        if (isDelivered) {
            throw new OrderAlreadyDeliveredException("Order Already Completed. All the items has been delivered.");
        }
    }
}
