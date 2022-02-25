package com.yunhalee.walkerholic.product.domain;

import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.product.exception.NotEnoughStockException;
import com.yunhalee.walkerholic.review.domain.Review;
import com.yunhalee.walkerholic.user.domain.User;
import java.math.BigDecimal;
import java.math.RoundingMode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
    private Float price;

//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<ProductImage> productImages = new ArrayList<>();

    @Embedded
    private ProductImages productImages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private ReviewInfo reviewInfo;

//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
//    @OrderBy("createdAt DESC")
//    private Set<Review> reviews = new HashSet<>();

    public Product(String name, String brand, Category category, Integer stock, Float price,
        String description) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.stock = stock;
        this.price = price;
        this.productImages = new ProductImages();
        this.reviewInfo = new ReviewInfo();
        this.description = description;
    }

    public Product(String name, String description, String brand, Category category, Integer stock,
        Float price, User user) {
        this.name = name;
        this.description = description;
        this.brand = brand;
        this.category = category;
        this.stock = stock;
        this.price = price;
        this.productImages = new ProductImages();
        this.user = user;
        this.reviewInfo = new ReviewInfo();
    }

    public static Product of(String name, String description, String brand, Category category,
        Integer stock, Float price, User user) {
        return new Product(name, description, brand, category, stock, price, user);
    }

    public void update(Product product) {
        this.name = product.getName();
        this.description = product.getDescription();
        this.brand = product.getBrand();
        this.category = product.getCategory();
        this.stock = product.getStock();
        this.price = product.getPrice();
    }

    public void addReview(Integer rating) {
        reviewInfo.addReview(rating);
    }

    public void editReview(Integer preRating, Integer postRating) {
        reviewInfo.editReview(preRating, postRating);
    }

    public void deleteReview(Integer rating) {
        reviewInfo.deleteReview(rating);
    }

    public BigDecimal getAverage() {
        return reviewInfo.getAverage();
    }

    //비지니스 로직
    public void addStock(Integer qty) {
        this.stock += qty;
    }

    public void removeStock(Integer qty) {
        Integer restStock = this.stock - qty;
        if (restStock < 0) {
            throw new NotEnoughStockException("Stock is not enough.");
        }
        this.stock = restStock;
    }

    public void addProductImage(ProductImage productImage) {
        this.productImages.addProductImage(productImage);
    }

    public void deleteProductImage(List<String> deletedImages) {
        this.productImages.deleteProductImage(deletedImages);
    }

    @Transient
    public String getMainImageUrl() {
        return this.productImages.getMainImageUrl();
    }

    @Transient
    public List<String> getImageUrls() {
        return this.productImages.getImageUrls();
    }

    public List<ProductImage> getProductImages() {
        return this.productImages.getProductImages();
    }



}
