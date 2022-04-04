package com.yunhalee.walkerholic.common.domain;

import com.yunhalee.walkerholic.product.domain.Product;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import lombok.Getter;

@MappedSuperclass
@Getter
public abstract class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    protected Integer id;

    @Column(nullable = false)
    protected Integer qty;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    protected Product product;

    public BigDecimal getAmount() {
        return product.getPrice().multiply(BigDecimal.valueOf(qty));
    }

    public Integer getProductStock() {
        return this.product.getStock();
    }

    public Integer getProductId() {
        return this.product.getId();
    }

    public String getProductName() {
        return this.product.getName();
    }

    public BigDecimal getProductPrice() {
        return this.product.getPrice();
    }

    public String getProductDescription() {
        return this.product.getDescription();
    }

    public String getProductBrand() {
        return this.product.getBrand();
    }

    public String getProductImageUrl() {
        return this.product.getMainImageUrl();
    }

}
