package com.yunhalee.walkerholic.product.dto;

import lombok.Getter;

@Getter
public class ProductSearchRequest {

    private Integer page;
    private String sort;
    private String category;
    private String keyword;

    public ProductSearchRequest() {
    }

    public ProductSearchRequest(Integer page, String sort, String category, String keyword) {
        this.page = page;
        this.sort = sort;
        this.category = category;
        this.keyword = keyword;
    }


}
