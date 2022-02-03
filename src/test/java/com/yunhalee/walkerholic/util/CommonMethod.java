package com.yunhalee.walkerholic.util;

import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.useractivity.domain.ActivityStatus;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;

public class CommonMethod {

    public static UserActivity userActivity(User user, Activity activity, ActivityStatus status) {
        return UserActivity.builder()
            .user(user)
            .activity(activity)
            .status(status).build();
    }
}
