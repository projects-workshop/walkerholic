package com.yunhalee.walkerholic.productImage.domain;

import com.yunhalee.walkerholic.product.domain.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import lombok.NonNull;

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

    @Builder
    public ProductImage(Integer id, @NonNull String name, @NonNull String filePath, Product product) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
        this.product = product;
    }

    public static ProductImage of(String name, String filePath, Product product) {
        return ProductImage.builder()
        .name(name)
        .filePath(filePath)
        .product(product).build();
    }
}
