package com.yunhalee.walkerholic.productImage.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class SimpleProductImageResponses {

    private List<String> imagesUrl;

    private SimpleProductImageResponses(List<String> imagesUrl) {
        this.imagesUrl = imagesUrl;
    }

    public static SimpleProductImageResponses of(List<String> imagesUrl) {
        return new SimpleProductImageResponses(imagesUrl);
    }
}
