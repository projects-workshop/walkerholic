package com.yunhalee.walkerholic.product.domain;

import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.product.exception.NotEnoughStockException;
import com.yunhalee.walkerholic.productImage.domain.ProductImage;
import com.yunhalee.walkerholic.user.domain.User;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "product",  indexes = {
    @Index(name = "idx_name", columnList = "name"),
    @Index(name = "idx_category_name", columnList = "category, name")
})
@Getter
@NoArgsConstructor
public class Product extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private ProductInfo productInfo;

    @Embedded
    private ProductImages productImages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private ReviewInfo reviewInfo;

    public Product(String name, String brand, Category category, Integer stock, Float price, String description) {
        this.productInfo = ProductInfo.of(name, description, brand, category, stock, price);
        this.productImages = new ProductImages();
        this.reviewInfo = new ReviewInfo();
    }

    public Product(String name, String description, String brand, Category category, Integer stock, Float price, User user) {
        this.productInfo = ProductInfo.of(name, description, brand, category, stock, price);
        this.productImages = new ProductImages();
        this.user = user;
        this.reviewInfo = new ReviewInfo();
    }

    public Product(Integer id, String name, String description, String brand, Category category, Integer stock, Float price, User user, ProductImage... productImages) {
        this.id = id;
        this.productInfo = ProductInfo.of(name, description, brand, category, stock, price);
        this.productImages = new ProductImages();
        this.user = user;
        this.reviewInfo = new ReviewInfo();
        this.productImages = ProductImages.of(productImages);
    }

    public static Product of(String name, String description, String brand, Category category, Integer stock, Float price, User user) {
        return new Product(name, description, brand, category, stock, price, user);
    }

    public void update(Product product) {
        productInfo.update(product.getProductInfo());
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

    public void addStock(Integer qty) {
        productInfo.addStock(qty);
    }

    public void removeStock(Integer qty) {
        productInfo.removeStock(qty);
    }

    public void addProductImage(ProductImage productImage) {
        this.productImages.addProductImage(productImage);
    }

    public void deleteProductImage(List<String> deletedImages) {
        this.productImages.deleteProductImage(deletedImages);
    }

    public String getName() {
        return productInfo.getName();
    }

    public String getDescription() {
        return productInfo.getDescription();
    }

    public String getBrand() {
        return productInfo.getBrand();
    }

    public Category getCategory() {
        return productInfo.getCategory();
    }

    public Integer getStock() {
        return productInfo.getStock();
    }

    public BigDecimal getPrice() {
        return productInfo.getPrice();
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

    public boolean isEnoughStock(Integer qty) {
        return this.productInfo.isEnoughStock(qty);
    }


}
