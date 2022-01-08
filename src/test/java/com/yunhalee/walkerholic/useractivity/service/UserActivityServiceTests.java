package com.yunhalee.walkerholic.useractivity.service;

import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
import com.yunhalee.walkerholic.user.domain.Level;
import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.useractivity.domain.ActivityStatus;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import com.yunhalee.walkerholic.useractivity.domain.UserActivityRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityResponses;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityRequest;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityResponse;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional
class UserActivityServiceTests {

    @InjectMocks
    private UserActivityService userActivityService;

    @MockBean
    private UserActivityRepository userActivityRepository;

    @MockBean
    private ActivityRepository activityRepository;

    @MockBean
    private UserRepository userRepository;

    private User user;
    private Activity activity;
    private UserActivity userActivity;

    @BeforeEach
    void setUp() {
        user = new User("testFirstName",
            "TestLastName",
            "test@example.com",
            "12345678",
            Role.USER);

        activity = Activity.builder()
            .name("testActivity")
            .score(500)
            .description("test-activity").build();

        userActivity = UserActivity.builder()
            .user(user)
            .activity(activity)
            .status(ActivityStatus.ONGOING).build();
    }

    @ParameterizedTest
    @CsvSource({"false", "false", "true"})
    @DisplayName("주어진 상태에 따른 새로운 사용자 액티비티를 생성하고 사용자 레벨을 조정한다.")
    void create_user_activity(boolean finished) {
        //given
        UserActivityRequest userActivityRequest = UserActivityRequest.builder()
            .userId(1)
            .activityId(1)
            .finished(finished).build();

        //when
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.of(user));
        when(activityRepository.findById(anyInt())).thenReturn(java.util.Optional.of(activity));
        UserActivityResponse response = userActivityService.create(userActivityRequest);

        //then
        assertEquals(response.getActivityId(), activity.getId());
        assertEquals(response.isFinished(), finished);
        assertEquals(user.getLevel(), finished ? Level.Silver : Level.Starter);
    }

    @Test
    @DisplayName("사용자 액티비티 상태가 완료되면 상태를 업데이트 하고 점수에 따라 레벨도 업데이트 한다.")
    void update_user_activity() {
        //given
        UserActivityRequest userActivityRequest = UserActivityRequest.builder()
            .userId(1)
            .activityId(1)
            .finished(true).build();

        //when
        when(userActivityRepository.findById(anyInt()))
            .thenReturn(java.util.Optional.of(userActivity));
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.of(user));
        when(activityRepository.findById(anyInt())).thenReturn(java.util.Optional.of(activity));
        UserActivityResponse response = userActivityService.update(userActivityRequest, 1);

        //then
        assertEquals(response.isFinished(), true);
        assertEquals(user.getLevel(), Level.Silver);
    }

    @Test
    @DisplayName("사용자의 사용자액티비티 목록을 조회한다.")
    public void getUserActivitiesByUserId() {
        //given
        UserActivity firstUserActivity = UserActivity.builder()
            .status(ActivityStatus.ONGOING)
            .user(user)
            .activity(activity).build();
        UserActivity secondUserActivity = UserActivity.builder()
            .status(ActivityStatus.FINISHED)
            .user(user)
            .activity(activity).build();

        //when
        Page<UserActivity> userActivityPage = new PageImpl<>(
            Arrays.asList(firstUserActivity, secondUserActivity));
        when(userActivityRepository.findByUserId(any(), anyInt())).thenReturn(userActivityPage);
        UserActivityResponses responses = userActivityService.userActivities(1, 1);

        //then
        assertThat(responses.getActivities().size()).isEqualTo(2);

    }

    @Test
    @DisplayName("사용자 액티비트를 삭제하고 삭제된 액티비티 점수에 따라 사용자 레벨을 수정한다.")
    public void deleteUserActivity() {
        //given
        userActivity = UserActivity.builder()
            .user(user)
            .activity(activity)
            .status(ActivityStatus.FINISHED).build();
        user.addUserActivity(userActivity);

        //when
        when(userRepository.findById(anyInt())).thenReturn(java.util.Optional.of(user));
        when(userActivityRepository.findById(anyInt()))
            .thenReturn(java.util.Optional.of(userActivity));
        userActivityService.deleteUserActivity(1, 1);

        //then
        verify(userActivityRepository).delete(any());
        assertEquals(user.getLevel(), Level.Starter);
    }
}

