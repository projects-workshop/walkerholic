package com.yunhalee.walkerholic.common.dto;

import com.yunhalee.walkerholic.common.domain.Item;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class ItemResponse {

    private final Integer id;
    private final Integer qty;
    private final Integer stock;
    private final Integer productId;
    private final String productName;
    private final BigDecimal productPrice;
    private final String productDescription;
    private final String productBrand;
    private final String productImageUrl;

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
