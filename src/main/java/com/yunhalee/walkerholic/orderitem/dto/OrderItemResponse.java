package com.yunhalee.walkerholic.orderitem.dto;

import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class OrderItemResponse {

    private Integer id;
    private Integer qty;
    private Integer stock;
    private Integer productId;
    private String productName;
    private BigDecimal productPrice;
    private String productDescription;
    private String productBrand;
    private String productImageUrl;

    private OrderItemResponse(OrderItem orderItem) {
        this.id = orderItem.getId();
        this.qty = orderItem.getQty();
        this.stock = orderItem.getProductStock();
        this.productId = orderItem.getProductId();
        this.productName = orderItem.getProductName();
        this.productPrice = orderItem.getProductPrice();
        this.productDescription = orderItem.getProductDescription();
        this.productBrand = orderItem.getProductBrand();
        this.productImageUrl = orderItem.getProductImageUrl();
    }

    public static OrderItemResponse of(OrderItem orderItem) {
        return new OrderItemResponse(orderItem);
    }
}
