package com.yunhalee.walkerholic.orderitem.dto;

import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDTO {

    private Integer id;

    private Integer qty;

    private Integer productId;

    private String productName;

    private Float productPrice;

    private String productDescription;

    private String productBrand;

    private String productImageUrl;

    public OrderItemDTO(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.qty = orderItem.getQty();
        this.productId = orderItem.getProduct().getId();
        this.productName = orderItem.getProduct().getName();
        this.productPrice = orderItem.getProduct().getPrice();
        this.productDescription = orderItem.getProduct().getDescription();
        this.productBrand = orderItem.getProduct().getBrand();
        this.productImageUrl = orderItem.getProduct().getMainImageUrl();
    }
}
