package com.yunhalee.walkerholic.order.dto;

import com.yunhalee.walkerholic.order.domain.Address;
import com.yunhalee.walkerholic.order.domain.Order;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PayOrderRequest {

    private Float shipping;
    private String paymentMethod;
    private AddressResponse address;

    public PayOrderRequest(String paymentMethod, Float shipping, AddressResponse address) {
        this.paymentMethod = paymentMethod;
        this.shipping = shipping;
        this.address = address;
    }

    public Order toOrder() {
        return new Order(
            paymentMethod,
            Address.builder()
                .name(address.getName())
                .country(address.getCountry())
                .city(address.getCity())
                .zipcode(address.getZipcode())
                .address(address.getAddress()).build(),
            BigDecimal.valueOf(shipping));
    }
}
