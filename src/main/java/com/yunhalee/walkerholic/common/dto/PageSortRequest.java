package com.yunhalee.walkerholic.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PageSortRequest {

    private Integer page;
    private String sort;

    public PageSortRequest(Integer page, String sort) {
        this.page = page;
        this.sort = sort;
    }
}
