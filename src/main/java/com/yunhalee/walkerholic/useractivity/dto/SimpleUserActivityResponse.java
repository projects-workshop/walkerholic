package com.yunhalee.walkerholic.useractivity.dto;

import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SimpleUserActivityResponse {

    private Integer id;
    private String status;
    private Integer userId;
    private String userImageUrl;
    private String userFullname;
    private LocalDateTime updatedAt;

    private SimpleUserActivityResponse(UserActivity userActivity) {
        this.id = userActivity.getId();
        this.status = userActivity.getStatusName();
        this.userId = userActivity.userId();
        this.userImageUrl = userActivity.userImageUrl();
        this.userFullname = userActivity.userFullName();
        this.updatedAt = userActivity.getUpdatedAt();
    }

    public static SimpleUserActivityResponse of(UserActivity userActivity) {
        return new SimpleUserActivityResponse(userActivity);
    }
}
