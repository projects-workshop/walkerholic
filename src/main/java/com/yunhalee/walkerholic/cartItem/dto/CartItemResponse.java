package com.yunhalee.walkerholic.cartItem.dto;

import com.yunhalee.walkerholic.cartItem.domain.CartItem;
import com.yunhalee.walkerholic.orderitem.domain.OrderItem;
import com.yunhalee.walkerholic.orderitem.dto.OrderItemResponse;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartItemResponse {

    private Integer id;
    private Integer qty;
    private Integer stock;
    private Integer productId;
    private String productName;
    private BigDecimal productPrice;
    private String productDescription;
    private String productBrand;
    private String productImageUrl;

    private CartItemResponse(CartItem cartItem) {
        this.id = cartItem.getId();
        this.qty = cartItem.getQty();
        this.stock = cartItem.getProductStock();
        this.productId = cartItem.getProductId();
        this.productName = cartItem.getProductName();
        this.productPrice = cartItem.getProductPrice();
        this.productDescription = cartItem.getProductDescription();
        this.productBrand = cartItem.getProductBrand();
        this.productImageUrl = cartItem.getProductImageUrl();
    }

    public static CartItemResponse of(CartItem cartItem){
        return new CartItemResponse(cartItem);
    }
}
