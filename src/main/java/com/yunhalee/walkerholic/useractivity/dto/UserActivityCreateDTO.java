package com.yunhalee.walkerholic.useractivity.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserActivityCreateDTO {

    private Integer id;

    private Integer activityId;

    private boolean finished;

    public UserActivityCreateDTO(Integer activityId, boolean finished) {
        this.activityId = activityId;
        this.finished = finished;
    }

    public UserActivityCreateDTO(Integer id, Integer activityId, boolean finished) {
        this.id = id;
        this.activityId = activityId;
        this.finished = finished;
    }
}
