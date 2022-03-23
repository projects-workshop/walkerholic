package com.yunhalee.walkerholic.order.dto;

import com.yunhalee.walkerholic.orderitem.dto.OrderItemRequest;
import com.yunhalee.walkerholic.useractivity.dto.AddressDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCreateDTO {

    private Integer id;

    private String paymentMethod;

    private Float shipping;

    private AddressDTO address;

    private List<OrderItemRequest> orderItems;

    private Integer userId;

    public OrderCreateDTO() {
    }

    public OrderCreateDTO(Integer id, String paymentMethod, Float shipping, AddressDTO address) {
        this.id = id;
        this.paymentMethod = paymentMethod;
        this.shipping = shipping;
        this.address = address;
    }

    public OrderCreateDTO(Integer id, String paymentMethod, AddressDTO address,
        List<OrderItemRequest> orderItems, Integer userId) {
        this.id = id;
        this.paymentMethod = paymentMethod;
        this.address = address;
        this.orderItems = orderItems;
        this.userId = userId;
    }

    public OrderCreateDTO(String paymentMethod, AddressDTO address,
        List<OrderItemRequest> orderItems, Integer userId) {
        this.paymentMethod = paymentMethod;
        this.address = address;
        this.orderItems = orderItems;
        this.userId = userId;
    }
}
