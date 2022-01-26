package com.yunhalee.walkerholic.useractivity.domain;

import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.user.domain.User;

public class UserActivityTest {

    public static UserActivity userActivity(User user, Activity activity, ActivityStatus status) {
        return UserActivity.builder()
            .user(user)
            .activity(activity)
            .status(status).build();
    }
}
