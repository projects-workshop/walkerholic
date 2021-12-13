package com.yunhalee.walkerholic.activity.dto;

import com.yunhalee.walkerholic.activity.domain.Activity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ActivityCreateDTO {

    private Integer id;

    private String name;

    private Integer score;

    private String description;

    private String imageUrl;

    public ActivityCreateDTO(Integer id, String name, Integer score, String description) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.description = description;
    }

    public ActivityCreateDTO(Activity activity) {
        this.id = activity.getId();
        this.name = activity.getName();
        this.score = activity.getScore();
        this.description = activity.getDescription();
        this.imageUrl = activity.getImageUrl();
    }

    public ActivityCreateDTO(String name, Integer score, String description) {
        this.name = name;
        this.score = score;
        this.description = description;
    }


}
