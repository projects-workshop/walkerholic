package com.yunhalee.walkerholic.product.dto;

import com.yunhalee.walkerholic.user.dto.SellerUserResponse;
import java.util.List;
import lombok.Getter;

@Getter
public class ProductResponses {

    private List<SimpleProductResponse> products;
    private SellerUserResponse seller;
    private Long totalElement;
    private Integer totalPage;

    public ProductResponses(List<SimpleProductResponse> products, Long totalElement, Integer totalPage) {
        this.products = products;
        this.totalElement = totalElement;
        this.totalPage = totalPage;
    }

    public static ProductResponses of(List<SimpleProductResponse> products, Long totalElement, Integer totalPage) {
        return new ProductResponses(products, totalElement, totalPage);
    }

    public ProductResponses(List<SimpleProductResponse> products, SellerUserResponse seller, Long totalElement, Integer totalPage) {
        this.products = products;
        this.seller = seller;
        this.totalElement = totalElement;
        this.totalPage = totalPage;
    }

    public static ProductResponses of(List<SimpleProductResponse> products, SellerUserResponse seller, Long totalElement, Integer totalPage) {
        return new ProductResponses(products, seller, totalElement, totalPage);
    }
}
