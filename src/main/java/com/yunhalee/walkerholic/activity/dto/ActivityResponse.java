package com.yunhalee.walkerholic.activity.dto;

import com.yunhalee.walkerholic.activity.domain.Activity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityResponse {

    private Integer id;

    private String name;

    private Integer score;

    private String description;

    private String imageUrl;

    public ActivityResponse(Activity activity) {
        this.id = activity.getId();
        this.name = activity.getName();
        this.score = activity.getScore();
        this.description = activity.getDescription();
        this.imageUrl = activity.getImageUrl();
    }

}
