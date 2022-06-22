package com.yunhalee.walkerholic.activity.dto;

import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import com.yunhalee.walkerholic.useractivity.dto.SimpleUserActivityResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@NoArgsConstructor
public class ActivityDetailResponse {

    private Integer id;

    private String name;

    private Integer score;

    private String description;

    private String imageUrl;

    private List<SimpleUserActivityResponse> activityUsers;

    public ActivityDetailResponse(Activity activity, List<SimpleUserActivityResponse> activityUsers) {
        this.id = activity.getId();
        this.name = activity.getName();
        this.score = activity.getScore();
        this.description = activity.getDescription();
        this.imageUrl = activity.getImageUrl();
        this.activityUsers = activityUsers;
    }

}
