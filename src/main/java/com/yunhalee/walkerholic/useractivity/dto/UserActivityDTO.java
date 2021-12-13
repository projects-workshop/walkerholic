package com.yunhalee.walkerholic.useractivity.dto;

import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UserActivityDTO {

    private Integer id;

    private Integer activityId;

    private String activityImageUrl;

    private String activityName;

    private Integer score;

    private boolean finished;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    public UserActivityDTO(UserActivity userActivity, boolean finished) {
        this.id = userActivity.getId();
        this.activityId = userActivity.getActivity().getId();
        this.activityName = userActivity.getActivity().getName();
        this.activityImageUrl = userActivity.getActivity().getImageUrl();
        this.score = userActivity.getActivity().getScore();
        this.finished = finished;
        this.createdAt = userActivity.getCreatedAt();
        this.updatedAt = userActivity.getUpdatedAt();
    }


}
