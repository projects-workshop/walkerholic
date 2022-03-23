package com.yunhalee.walkerholic.order.dto;

import com.yunhalee.walkerholic.order.domain.Address;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemRequest;
import com.yunhalee.walkerholic.useractivity.dto.AddressDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
public class PayOrderRequest {

    private Float shipping;
    private String paymentMethod;
    private AddressDTO address;

    public PayOrderRequest(String paymentMethod, Float shipping, AddressDTO address) {
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
            shipping);
    }
}
