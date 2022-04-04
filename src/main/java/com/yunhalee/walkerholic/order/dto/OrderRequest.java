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
    private AddressResponse address;
    private List<OrderItemRequest> orderItems;

    public OrderRequest(Integer userId, Float shipping, String paymentMethod, AddressResponse address, List<OrderItemRequest> orderItems) {
        this.userId = userId;
        this.shipping = shipping;
        this.paymentMethod = paymentMethod;
        this.address = address;
        this.orderItems = orderItems;
    }

    public Order toOrder() {
        return Order.of(userId,
            BigDecimal.valueOf(shipping),
            paymentMethod,
            Address.builder()
                .name(address.getName())
                .country(address.getCountry())
                .city(address.getCity())
                .zipcode(address.getZipcode())
                .address(address.getAddress()).build());
    }
}
