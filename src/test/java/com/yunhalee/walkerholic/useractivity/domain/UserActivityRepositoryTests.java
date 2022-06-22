package com.yunhalee.walkerholic.useractivity.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.yunhalee.walkerholic.RepositoryTest;
import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.activity.domain.ActivityTest;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserTest;
import com.yunhalee.walkerholic.util.CommonMethod;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class UserActivityRepositoryTests extends RepositoryTest {

    private Activity activity;
    private User user;
    private UserActivity firstUserActivity;
    private UserActivity secondUserActivity;
    private UserActivity thirdUserActivity;


    @Before
    public void setUp() {
        user = userRepository.save(UserTest.USER);
        activity = activityRepository.save(ActivityTest.ACTIVITY);
        firstUserActivity = userActivityRepository.save(CommonMethod.userActivity(user, activity, ActivityStatus.ONGOING));
        secondUserActivity = userActivityRepository.save(CommonMethod.userActivity(user, activity, ActivityStatus.ONGOING));
        thirdUserActivity = userActivityRepository.save(CommonMethod.userActivity(user, activity, ActivityStatus.FINISHED));
    }

    @Test
    @DisplayName("사용자 아이디를 이용하여 사용자 액티비티 목록을 특정 크기만큼 조회한다.")
    public void find_by_user_id() {
        //when
        Pageable pageable = PageRequest.of(0, 2);
        List<UserActivity> userActivities = userActivityRepository.findByUserId(pageable, user.getId()).getContent();

        //then
        assertThat(userActivities.size()).isEqualTo(2);
        assertThat(userActivities.equals(Arrays.asList(thirdUserActivity, secondUserActivity))).isTrue();
    }


}
