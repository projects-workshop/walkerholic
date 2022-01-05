package com.yunhalee.walkerholic.useractivity.service;

import com.yunhalee.walkerholic.user.domain.Level;
import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.useractivity.domain.UserActivityRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityListResponse;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityRequest;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityResponse;
import java.util.NoSuchElementException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class UserActivityServiceTests {

    @Autowired
    UserActivityService userActivityService;

    @Autowired
    UserActivityRepository userActivityRepository;

    @Autowired
    UserRepository userRepository;

    private Integer userId;
    private static final Integer ACTIVITY_ID = 1;
    private static final boolean FINISHED = false;

    private UserActivityResponse userActivityResponse;

    @Test
    public void create_user_activity() {
        //given
        createUserActivity();

        //when

        //then
        assertEquals(userActivityResponse.getActivityId(), ACTIVITY_ID);
        assertEquals(userActivityResponse.isFinished(), FINISHED);
        assertEquals(userActivityResponse.getLevel(), Level.Starter.getName());
    }

    @Test
    public void update_user_activity() {
        //given
        createUserActivity();
        UserActivityRequest userActivityRequest = UserActivityRequest.builder()
            .userId(userId)
            .activityId(ACTIVITY_ID)
            .finished(true).build();

        //when
        UserActivityResponse updatedResponse = userActivityService
            .update(userActivityRequest, userActivityResponse.getId());
        //then
        assertEquals(updatedResponse.getActivityId(), ACTIVITY_ID);
        assertEquals(updatedResponse.isFinished(), true);
        assertEquals(updatedResponse.getLevel(), Level.Bronze.getName());
    }

    @Test
    public void getUserActivitiesByUserId() {
        //given
        createUserActivity();

        //when
        UserActivityListResponse userActivities = userActivityService.userActivities(1, userId);

        //then
        assertThat(userActivities.getActivities().size()).isGreaterThan(0);

    }

    @Test
    public void deleteUserActivity() {
        //given
        createUserActivity();
        Integer id = userActivityResponse.getId();

        //when
        userActivityService.deleteUserActivity(id, userId);

        //then
        assertThatThrownBy(() -> userActivityRepository.findById(id).get())
            .isInstanceOf(NoSuchElementException.class);
        assertEquals(userRepository.findById(userId).get().getLevel(), Level.Starter);
    }


    void createUserActivity() {
        User user = new User("testName", "testName", "test@example.com", "12345678", Role.USER);
        user.setLevel(Level.Starter);
        userRepository.save(user);
        userId = user.getId();
        UserActivityRequest userActivityRequest = UserActivityRequest.builder()
            .userId(user.getId())
            .activityId(ACTIVITY_ID)
            .finished(FINISHED).build();

        userActivityResponse = userActivityService.create(userActivityRequest);
    }
}
