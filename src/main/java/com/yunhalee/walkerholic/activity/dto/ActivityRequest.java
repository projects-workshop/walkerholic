package com.yunhalee.walkerholic.activity.dto;

import com.yunhalee.walkerholic.activity.domain.Activity;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityRequest {

    @NotNull
    private String name;

    @NotNull
    private Integer score;

    @NotNull
    private String description;

    private String imageUrl;

    @Builder
    public ActivityRequest(String name, Integer score, String description, String imageUrl) {
        this.name = name;
        this.score = score;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public Activity toActivity() {
        return new Activity(name, score, description, imageUrl);
    }

}
