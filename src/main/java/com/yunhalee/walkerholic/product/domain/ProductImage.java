package com.yunhalee.walkerholic.product.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "product_image")
@Getter
@NoArgsConstructor
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_image_id")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, name = "file_path")
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private ProductImage(String name, String filePath, Product product) {
        this.name = name;
        this.filePath = filePath;
        this.product = product;
    }

    public ProductImage(Integer id, String name, String filePath) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
    }

    public static ProductImage of(String name, String filePath, Product product) {
        return new ProductImage(name, filePath, product);
    }
}
