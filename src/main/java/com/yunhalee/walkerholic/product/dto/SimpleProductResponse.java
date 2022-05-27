package com.yunhalee.walkerholic.product.dto;

import com.yunhalee.walkerholic.product.domain.Product;
import java.math.BigDecimal;
import lombok.Getter;

import java.util.List;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SimpleProductResponse {

    private Integer id;

    private String name;

    private String brand;

    private String category;

    private Integer stock;

    private BigDecimal price;

    private BigDecimal average;

    private List<String> imagesUrl;

    private String description;

    public SimpleProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.brand = product.getBrand();
        this.category = product.getCategory().name();
        this.stock = product.getStock();
        this.price = product.getPrice();
        this.average = product.getAverage();
        this.imagesUrl = product.getImageUrls();
        this.description = product.getDescription();
    }

    public static SimpleProductResponse of(Product product) {
        return new SimpleProductResponse(product);
    }
}
