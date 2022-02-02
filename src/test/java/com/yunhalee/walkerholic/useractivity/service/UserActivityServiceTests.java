package com.yunhalee.walkerholic.useractivity.service;

import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
import com.yunhalee.walkerholic.activity.domain.FakeActivityRepository;
import com.yunhalee.walkerholic.user.domain.FakeUserRepository;
import com.yunhalee.walkerholic.user.domain.Level;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.useractivity.domain.ActivityStatus;
import com.yunhalee.walkerholic.useractivity.domain.FakeUserActivityRepository;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import com.yunhalee.walkerholic.useractivity.domain.UserActivityRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.useractivity.domain.UserActivityTest;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityResponses;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityRequest;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class UserActivityServiceTests {

    private UserRepository userRepository = new FakeUserRepository();
    private ActivityRepository activityRepository = new FakeActivityRepository();
    private UserActivityRepository userActivityRepository = new FakeUserActivityRepository();

    private UserActivityService userActivityService = new UserActivityService(userActivityRepository, userRepository, activityRepository);


    @ParameterizedTest
    @CsvSource({"false", "false", "true"})
    @DisplayName("주어진 상태에 따른 새로운 사용자 액티비티를 생성하고 사용자 레벨을 조정한다.")
    void create_user_activity(boolean finished) {
        //given
        UserActivityRequest userActivityRequest = userActivityRequest(1, 1, finished);
        Activity activity = activityRepository.findById(1).get();

        //when
        UserActivityResponse response = userActivityService.create(userActivityRequest);

        //then
        assertEquals(response.getActivityName(), activity.getName());
        assertEquals(response.isFinished(), finished);
        assertEquals(response.getLevel(), finished ? Level.Bronze.getName() : Level.Starter.getName());
    }

    @Test
    @DisplayName("사용자 액티비티 상태가 완료되면 상태를 업데이트 하고 점수에 따라 레벨도 업데이트 한다.")
    void update_user_activity() {
        //given
        UserActivityRequest userActivityRequest = userActivityRequest(1, 1, true);

        //when
        UserActivityResponse response = userActivityService.update(userActivityRequest, 1);

        //then
        assertEquals(response.isFinished(), true);
        assertEquals(response.getLevel(), Level.Bronze.getName());
    }

    @Test
    @DisplayName("사용자의 사용자액티비티 목록을 조회한다.")
    public void getUserActivitiesByUserId() {
        //given

        //when
        UserActivityResponses responses = userActivityService.userActivities(1, 1);

        //then
        assertThat(responses.getActivities().size()).isEqualTo(2);

    }

    @Test
    @DisplayName("사용자 액티비트를 삭제하고 삭제된 액티비티 점수에 따라 사용자 레벨을 수정한다.")
    public void deleteUserActivity() {
        //given
        Integer userId = 1;
        User user = userRepository.findById(userId).get();
        Activity activity = activityRepository.findById(1).get();
        UserActivity userActivity = UserActivityTest.userActivity(user, activity, ActivityStatus.FINISHED);
        user.updateLevel(userActivity);

        //when
        userActivityService.deleteUserActivity(1, userId);

        //then
        assertEquals(userRepository.findById(userId).get().getLevel(), Level.Starter);
    }

    private UserActivityRequest userActivityRequest(Integer userId, Integer activityId, boolean finished) {
        return UserActivityRequest.builder()
            .userId(userId)
            .activityId(activityId)
            .finished(finished).build();
    }

}

