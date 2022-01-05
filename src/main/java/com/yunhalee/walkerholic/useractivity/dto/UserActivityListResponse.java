package com.yunhalee.walkerholic.useractivity.dto;

import com.yunhalee.walkerholic.useractivity.domain.ActivityStatus;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class UserActivityListResponse {

    private List<UserActivityResponse> activities;

    private Integer totalPage;

    private long totalElement;

    private int score;


    public UserActivityListResponse(List<UserActivity> userActivities,
        Page<UserActivity> userActivityPage, int score) {
        this.activities = UserActivityResponse.activities(userActivities);
        this.totalPage = userActivityPage.getTotalPages();
        this.totalElement = userActivityPage.getTotalElements();
        this.score = score;
    }

    @Getter
    static class UserActivityResponse {

        private Integer id;

        private Integer activityId;

        private String activityImageUrl;

        private String activityName;

        private Integer score;

        private boolean finished;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        static List<UserActivityResponse> activities(List<UserActivity> userActivities) {
            List<UserActivityResponse> userActivitiesResponse = userActivities.stream()
                .map(UserActivityResponse::new)
                .collect(Collectors.toList());
            return userActivitiesResponse;
        }

        public UserActivityResponse(UserActivity userActivity) {
            this.id = userActivity.getId();
            this.activityId = userActivity.getActivity().getId();
            this.activityName = userActivity.getActivity().getName();
            this.activityImageUrl = userActivity.getActivity().getImageUrl();
            this.score = userActivity.getActivity().getScore();
            this.finished = userActivity.getStatus() == ActivityStatus.FINISHED;
            this.createdAt = userActivity.getCreatedAt();
            this.updatedAt = userActivity.getUpdatedAt();
        }

    }

}
