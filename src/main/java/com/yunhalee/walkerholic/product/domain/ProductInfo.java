package com.yunhalee.walkerholic.product.domain;

import com.yunhalee.walkerholic.product.exception.NotEnoughStockException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Getter;

@Embeddable
@Getter
public class ProductInfo {

    @Column(nullable = false, length = 45)
    private String name;

    private String description;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private BigDecimal price;

    public ProductInfo() {
    }

    private ProductInfo(String name, String description, String brand, Category category, Integer stock, Float price) {
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.category = category;
        this.stock = stock;
        this.price = BigDecimal.valueOf(price).setScale(2, RoundingMode.HALF_EVEN);
    }

    public static ProductInfo of(String name, String description, String brand, Category category, Integer stock, Float price) {
        return new ProductInfo(name, description, brand, category, stock, price);
    }

    public void update(ProductInfo productInfo) {
        this.name = productInfo.getName();
        this.description = productInfo.getDescription();
        this.brand = productInfo.getBrand();
        this.category = productInfo.getCategory();
        this.stock = productInfo.getStock();
        this.price = productInfo.getPrice();
    }

    public void addStock(Integer qty) {
        this.stock += qty;
    }

    public void removeStock(Integer qty) {
        if (!isEnoughStock(qty)) {
            throw new NotEnoughStockException("Stock is not enough.");
        }
        this.stock = this.stock - qty;
    }

    public boolean isEnoughStock(Integer qty){
        return this.stock >= qty;
    }
}
