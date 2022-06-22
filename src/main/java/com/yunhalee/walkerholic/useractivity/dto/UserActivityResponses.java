package com.yunhalee.walkerholic.useractivity.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserActivityResponses {

    private List<UserActivityResponse> activities;

    private Integer totalPage;

    private long totalElement;

    private int score;

    public UserActivityResponses(List<UserActivityResponse> userActivities, Long totalElement, Integer totalPage, int score) {
        this.activities = userActivities;
        this.totalPage = totalPage;
        this.totalElement = totalElement;
        this.score = score;
    }

}
