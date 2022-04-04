package com.yunhalee.walkerholic.common.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class ItemResponses {

    private List<ItemResponse> items = new ArrayList<>();

    public ItemResponses() {
        this.items = new ArrayList<>();
    }

    private ItemResponses(List<ItemResponse> items) {
        this.items = items;
    }

    public static ItemResponses of(List<ItemResponse> items) {
        return new ItemResponses(items);
    }


}
