package com.yunhalee.walkerholic.order.dto;

import com.yunhalee.walkerholic.order.domain.Address;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemRequest;
import com.yunhalee.walkerholic.useractivity.dto.AddressDTO;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class OrderRequest {

    private Float shipping;
    private String paymentMethod;
    private AddressDTO address;
    private List<OrderItemRequest> orderItems;
    private Integer userId;

    public OrderRequest(Float shipping, String paymentMethod, AddressDTO address, List<OrderItemRequest> orderItems, Integer userId) {
        this.shipping = shipping;
        this.paymentMethod = paymentMethod;
        this.address = address;
        this.orderItems = orderItems;
        this.userId = userId;
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
