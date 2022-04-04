package com.yunhalee.walkerholic.order.dto;

import com.yunhalee.walkerholic.order.domain.Address;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemRequest;
import java.math.BigDecimal;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequest {

    private Integer userId;
    private Float shipping;
    private String paymentMethod;
    private String transactionId;
    private AddressResponse address;

    public OrderRequest(Integer userId, Float shipping, String paymentMethod, String transactionId, AddressResponse address) {
        this.userId = userId;
        this.shipping = shipping;
        this.paymentMethod = paymentMethod;
        this.transactionId = transactionId;
        this.address = address;
    }

    public Order toOrder() {
        return Order.of(userId,
            BigDecimal.valueOf(shipping),
            paymentMethod,
            transactionId,
            Address.builder()
                .name(address.getName())
                .country(address.getCountry())
                .city(address.getCity())
                .zipcode(address.getZipcode())
                .address(address.getAddress()).build());
    }
}
