package com.yunhalee.walkerholic.product.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductCreateDTO {

    private Integer id;

    private String name;

    private String description;

    private String brand;

    private String category;

    private Integer stock;

    private Float price;

    private Integer userId;

    public ProductCreateDTO(String name, String description, String brand, String category,
        Integer stock, Float price, Integer userId) {
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.category = category;
        this.stock = stock;
        this.price = price;
        this.userId = userId;
    }

    public ProductCreateDTO(Integer id, String name, String description, String brand,
        String category, Integer stock, Float price, Integer userId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.category = category;
        this.stock = stock;
        this.price = price;
        this.userId = userId;
    }

}
