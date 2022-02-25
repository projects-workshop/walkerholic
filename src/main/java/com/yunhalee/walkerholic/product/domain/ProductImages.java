package com.yunhalee.walkerholic.product.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class ProductImages {

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductImage> productImages = new ArrayList<>();

    public ProductImages() {
    }

    private ProductImages(List<ProductImage> productImages) {
        this.productImages = productImages;
    }

    public static ProductImages of(ProductImage... productImages) {
        return new ProductImages(new ArrayList<>(Arrays.asList(productImages)));
    }

    public void addProductImage(ProductImage productImage) {
        this.productImages.add(productImage);
    }

    public List<ProductImage> getProductImages() {
        return Collections.unmodifiableList(productImages);
    }

    public String getMainImageUrl() {
        return productImages.get(0).getFilePath();
    }

    public List<String> getImageUrls() {
        return this.productImages.stream()
            .map(ProductImage::getFilePath)
            .collect(Collectors.toList());
    }

    public void deleteProductImage(List<String> deletedImages) {
        this.productImages = this.productImages.stream()
            .filter(productImage -> !deletedImages.contains(productImage.getFilePath()))
            .collect(Collectors.toList());
    }
}
