package com.yunhalee.walkerholic.useractivity.dto;

import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.useractivity.domain.ActivityStatus;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Getter
@NoArgsConstructor
public class UserActivityRequest {

    private Integer userId;

    private Integer activityId;

    private Integer distance;

    private boolean finished;

    @Builder
    public UserActivityRequest(@NonNull Integer userId, Integer distance, @NonNull Integer activityId, @NonNull boolean finished) {
        this.userId = userId;
        this.distance = distance;
        this.activityId = activityId;
        this.finished = finished;
    }

    public UserActivity toUserActivity(User user, Activity activity) {
        return UserActivity.builder()
            .status(finished ? ActivityStatus.FINISHED : ActivityStatus.ONGOING)
            .distance(distance)
            .user(user)
            .activity(activity).build();
    }


}
