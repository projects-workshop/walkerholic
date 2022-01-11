package com.yunhalee.walkerholic.useractivity.domain;

import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import java.util.Arrays;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserActivityRepositoryTests {

    @Autowired
    private UserActivityRepository userActivityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    private UserActivity userActivity;

    private Activity activity;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User("testFirstName",
            "TestLastName",
            "test@example.com",
            "12345678",
            Role.USER);
        userRepository.save(user);

        activity = Activity.builder()
            .name("testActivity")
            .score(500)
            .description("test-activity").build();
        activityRepository.save(activity);

        userActivity = UserActivity.builder()
            .user(user)
            .activity(activity)
            .status(ActivityStatus.ONGOING).build();
    }

    @Test
    @DisplayName("주어진 정보대로 사용자 액티비티를 생성한다.")
    public void create_user_activity() {
        //given
        setUp();

        //when
        UserActivity createdUserActivity = userActivityRepository.save(userActivity);

        //then
        Assertions.assertAll(
            () -> assertThat(createdUserActivity.getId()).isNotNull(),
            () -> assertThat(createdUserActivity.getStatus()).isEqualTo(userActivity.getStatus()),
            () -> assertThat(createdUserActivity.getUser().getId()).isEqualTo(user.getId()),
            () -> assertThat(createdUserActivity.getActivity().getId()).isEqualTo(activity.getId())
        );
    }

    @Test
    @DisplayName("사용자 아이디를 이용하여 사용자 액티비티 목록을 특정 크기만큼 조회한다.")
    public void getByUserId() {
        //given
        setUp();
        saveAll();

        //when
        Pageable pageable = PageRequest.of(0, 2);
        List<UserActivity> userActivities = userActivityRepository
            .findByUserId(pageable, user.getId())
            .getContent();

        //then
        assertThat(userActivities).hasSize(pageable.getPageSize());
        for (UserActivity userActivity : userActivities) {
            assertThat(userActivity.getUser().getId()).isEqualTo(user.getId());
        }
    }

    private void saveAll() {
        UserActivity firstUserActivity = userActivity(user, activity, ActivityStatus.ONGOING);
        UserActivity secondUserActivity = userActivity(user, activity, ActivityStatus.FINISHED);
        userActivityRepository
            .saveAll(Arrays.asList(userActivity, firstUserActivity, secondUserActivity));
    }

    private UserActivity userActivity(User user, Activity activity, ActivityStatus status) {
        return UserActivity.builder()
            .user(user)
            .activity(activity)
            .status(status).build();
    }


}
