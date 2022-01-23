package com.yunhalee.walkerholic.useractivity;


import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.useractivity.domain.ActivityStatus;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@Ignore
public class BaseUserActivityTests {

    protected UserActivity userActivity(User user, Activity activity, ActivityStatus status) {
        return UserActivity.builder()
            .user(user)
            .activity(activity)
            .status(status).build();
    }

}
