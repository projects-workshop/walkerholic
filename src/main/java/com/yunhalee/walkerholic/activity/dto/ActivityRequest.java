package com.yunhalee.walkerholic.activity.dto;

import com.yunhalee.walkerholic.activity.domain.Activity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActivityRequest {

    private String name;

    private Integer score;

    private String description;

    public ActivityRequest(String name, Integer score, String description) {
        this.name = name;
        this.score = score;
        this.description = description;
    }


    public Activity toActivity() {
        return new Activity(name, score, description);
    }

}
