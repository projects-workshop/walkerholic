package com.yunhalee.walkerholic.useractivity.domain;

import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
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

    private UserActivity userActivity;

    @Test
    public void create_user_activity() {
        //given
        createSetUp();
        //when
        UserActivity createdUserActivity = userActivityRepository.save(userActivity);

        //then
        checkEqual(createdUserActivity);
    }

    @Test
    public void update_user_activity() {
        //given
        createSetUp();
        UserActivity requestUserActivity = updateSetUp();
        userActivity.update(requestUserActivity);

        //when
        UserActivity updatedUserActivity = userActivityRepository.save(userActivity);

        //then
        assertThat(updatedUserActivity.getId()).isEqualTo(userActivity.getId());
        checkEqual(updatedUserActivity);
    }

    @Test
    public void getByUserId() {
        //given
        createSetUp();
        Integer userId = userActivity.getUser().getId();

        //when
        Pageable pageable = PageRequest.of(0, USER_ACTIVITY_PER_PAGE);
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
        createSetUp();
        Integer userActivityId = userActivity.getId();

        //when
        userActivityRepository.deleteById(userActivityId);

        //then
        assertThat(userActivityRepository.findById(userActivityId)).isNull();
    }

    private void createSetUp() {
        userActivity = UserActivity.builder()
            .status(ActivityStatus.ONGOING)
            .user(userRepository.findById(1).get())
            .activity(activityRepository.findById(1).get()).build();
    }

    private UserActivity updateSetUp() {
        return UserActivity.builder()
            .status(ActivityStatus.FINISHED)
            .user(userActivity.getUser())
            .activity(userActivity.getActivity()).build();
    }

    private void checkEqual(UserActivity changedUserActivity) {
        assertThat(changedUserActivity.getId()).isNotNull();
        assertThat(changedUserActivity.getStatus()).isEqualTo(userActivity.getStatus());
        assertThat(changedUserActivity.getUser().getId()).isEqualTo(userActivity.getUser().getId());
        assertThat(changedUserActivity.getActivity().getId())
            .isEqualTo(userActivity.getActivity().getId());
    }

}
