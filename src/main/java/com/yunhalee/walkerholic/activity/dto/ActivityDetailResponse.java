package com.yunhalee.walkerholic.activity.dto;

import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Getter
@Setter
public class ActivityDetailResponse {

    private Integer id;

    private String name;

    private Integer score;

    private String description;

    private String imageUrl;

    private List<ActivityUser> activityUsers;

    public ActivityDetailResponse(Activity activity) {
        this.id = activity.getId();
        this.name = activity.getName();
        this.score = activity.getScore();
        this.description = activity.getDescription();
        this.imageUrl = activity.getImageUrl();
        this.activityUsers = ActivityUser.userActivityList(activity.getUserActivities());
    }

    @Getter
    static class ActivityUser {

        private Integer id;
        private String status;
        private Integer userId;
        private String userImageUrl;
        private String userFullname;
        private LocalDateTime updatedAt;

        static List<ActivityUser> userActivityList(Set<UserActivity> userActivities) {
            List<ActivityUser> activityUsers = new ArrayList<>();
            userActivities
                .forEach(userActivity -> activityUsers.add(new ActivityUser(userActivity)));
            return activityUsers;
        }

        public ActivityUser(UserActivity userActivity) {
            this.id = userActivity.getId();
            this.status = userActivity.getStatus().name();
            this.userId = userActivity.getUser().getId();
            this.userImageUrl = userActivity.getUser().getImageUrl();
            this.userFullname = userActivity.getUser().getFullName();
            this.updatedAt = userActivity.getUpdatedAt();
        }
    }
}
