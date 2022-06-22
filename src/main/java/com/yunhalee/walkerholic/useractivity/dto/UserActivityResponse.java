package com.yunhalee.walkerholic.useractivity.dto;

import com.yunhalee.walkerholic.useractivity.domain.ActivityStatus;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import lombok.Getter;

import java.time.LocalDateTime;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserActivityResponse {

    private Integer id;

    private Integer activityId;

    private String activityImageUrl;

    private String activityName;

    private Integer score;

    private boolean finished;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String level;

    public UserActivityResponse(UserActivity userActivity, String level) {
        this.id = userActivity.getId();
        this.activityId = userActivity.activityId();
        this.activityName = userActivity.activityName();
        this.activityImageUrl = userActivity.activityImageUrl();
        this.score = userActivity.activityScore();
        this.finished = userActivity.getStatus() == ActivityStatus.FINISHED;
        this.createdAt = userActivity.getCreatedAt();
        this.updatedAt = userActivity.getUpdatedAt();
        this.level = level;
    }

    public UserActivityResponse(UserActivity userActivity) {
        this.id = userActivity.getId();
        this.activityId = userActivity.activityId();
        this.activityName = userActivity.activityName();
        this.activityImageUrl = userActivity.activityImageUrl();
        this.score = userActivity.activityScore();
        this.finished = userActivity.getStatus() == ActivityStatus.FINISHED;
        this.createdAt = userActivity.getCreatedAt();
        this.updatedAt = userActivity.getUpdatedAt();
    }


}
