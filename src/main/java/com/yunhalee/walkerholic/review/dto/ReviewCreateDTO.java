package com.yunhalee.walkerholic.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewCreateDTO {

    private Integer id;

    private Integer rating;

    private String comment;

    private Integer productId;

    private Integer userId;

    public ReviewCreateDTO(Integer rating, String comment, Integer productId, Integer userId) {
        this.rating = rating;
        this.comment = comment;
        this.productId = productId;
        this.userId = userId;
    }


    public ReviewCreateDTO(Integer id, Integer rating, String comment) {
        this.id = id;
        this.rating = rating;
        this.comment = comment;
    }
}
