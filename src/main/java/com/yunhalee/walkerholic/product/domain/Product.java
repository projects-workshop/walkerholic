package com.yunhalee.walkerholic.product.domain;

import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.product.exception.NotEnoughStockException;
import com.yunhalee.walkerholic.review.domain.Review;
import com.yunhalee.walkerholic.user.domain.User;
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
@Setter
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

    private Float average;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdAt DESC")
    private Set<Review> reviews = new HashSet<>();

    public Product(String name, String brand, Category category, Integer stock, Float price) {
        this.name = name;
        this.brand = brand;
        this.category = category;
        this.stock = stock;
        this.price = price;
    }

    public void addReview(Review review) {
        reviews.add(review);
        review.changeProduct(this);

        Integer sum = reviews.stream().mapToInt(reviews -> reviews.getRating()).sum();
        changeAverage(sum, reviews.size());
    }

    public void editReview(Integer preRating, Integer postRating) {
        Integer sum = reviews.stream().mapToInt(reviews -> reviews.getRating()).sum();
        sum = (sum - preRating + postRating);
        changeAverage(sum, reviews.size());
    }

    public void deleteReview(Integer rating) {
        Integer sum = reviews.stream().mapToInt(reviews -> reviews.getRating()).sum();
        sum -= rating;
        changeAverage(sum, reviews.size() - 1);
    }

    private void changeAverage(Integer sum, int size) {
        if (size == 0) {
            this.average = 0f;
            return;
        }
        this.average = (float) (Math.round(sum / (long) (size) * 100) / 100.0);
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
        this.productImages.add(productImage);
    }

    @Transient
    public String getMainImageUrl() {
        return this.productImages.get(0).getFilePath();
    }

    @Transient
    public List<String> getImagesUrl() {
        List<String> imagesUrl = new ArrayList<>();
        this.productImages.forEach(productImage -> imagesUrl.add(productImage.getFilePath()));
        return imagesUrl;
    }


}
