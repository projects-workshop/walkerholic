package com.yunhalee.walkerholic.useractivity.domain;

import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.activity.domain.ActivityStatus;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
import com.yunhalee.walkerholic.useractivity.domain.UserActivityRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Rollback(false)
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class UserActivityRepositoryTests {

    @Autowired
    UserActivityRepository userActivityRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ActivityRepository activityRepository;

    public static final int USER_ACTIVITY_PER_PAGE = 10;

    @Test
    public void createUserActivity() {
        //given
        ActivityStatus activityStatus = ActivityStatus.ONGOING;
        Integer userId = 1;
        Integer activityId = 1;
        User user = userRepository.findById(userId).get();
        Activity activity = activityRepository.findById(activityId).get();

        UserActivity userActivity = new UserActivity();
        userActivity.setStatus(activityStatus);
        userActivity.setUser(user);
        userActivity.setActivity(activity);

        //when
        UserActivity userActivity1 = userActivityRepository.save(userActivity);

        //then
        assertThat(userActivity1.getId()).isNotNull();
        assertThat(userActivity1.getStatus()).isEqualTo(activityStatus);
        assertThat(userActivity1.getUser().getId()).isEqualTo(userId);
        assertThat(userActivity1.getActivity().getId()).isEqualTo(activityId);
    }

    @Test
    public void updateUserActivity() {
        //given
        ActivityStatus updatedActivityStatus = ActivityStatus.FINISHED;
        Integer id = 1;
        UserActivity userActivity = userActivityRepository.findById(id).get();
//        ActivityStatus originalActivityStatus = userActivity.getStatus();
        userActivity.setStatus(updatedActivityStatus);

        //when
        UserActivity userActivity1 = userActivityRepository.save(userActivity);

        //then
        assertThat(userActivity1.getId()).isEqualTo(id);
        assertThat(userActivity1.getStatus()).isEqualTo(updatedActivityStatus);
    }

    @Test
    public void getByUserId() {
        //given
        Integer userId = 1;
        Integer page = 1;

        //when
        Pageable pageable = PageRequest.of(page - 1, USER_ACTIVITY_PER_PAGE);
        Page<UserActivity> userActivityPage = userActivityRepository.findByUserId(pageable, userId);
        List<UserActivity> userActivityList = userActivityPage.getContent();

        //then
        for (UserActivity userActivity : userActivityList) {
            assertThat(userActivity.getUser().getId()).isEqualTo(userId);
        }
    }

    @Test
    public void deleteById() {
        //given
        Integer id = 1;

        //when
        userActivityRepository.deleteById(1);

        //then
        assertThat(userActivityRepository.findById(id)).isNull();
    }
}
