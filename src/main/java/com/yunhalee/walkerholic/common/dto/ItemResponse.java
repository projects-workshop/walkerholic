package com.yunhalee.walkerholic.common.dto;

import com.yunhalee.walkerholic.common.domain.Item;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ItemResponse {

    private Integer id;
    private Integer qty;
    private Integer stock;
    private Integer productId;
    private String productName;
    private BigDecimal productPrice;
    private String productDescription;
    private String productBrand;
    private String productImageUrl;

    private ItemResponse(Item item) {
        this.id = item.getId();
        this.qty = item.getQty();
        this.stock = item.getProductStock();
        this.productId = item.getProductId();
        this.productName = item.getProductName();
        this.productPrice = item.getProductPrice();
        this.productDescription = item.getProductDescription();
        this.productBrand = item.getProductBrand();
        this.productImageUrl = item.getProductImageUrl();
    }

    public static ItemResponse of(Item item) {
        return new ItemResponse(item);
    }
}
