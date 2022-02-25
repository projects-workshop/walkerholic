package com.yunhalee.walkerholic.useractivity.service;

import com.yunhalee.walkerholic.activity.service.ActivityService;
import com.yunhalee.walkerholic.user.service.UserService;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityResponses;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityRequest;
import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import com.yunhalee.walkerholic.useractivity.domain.UserActivityRepository;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityResponse;
import com.yunhalee.walkerholic.useractivity.exception.UserActivityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserActivityService {

    private static final int USER_ACTIVITY_PER_PAGE = 10;

    private UserActivityRepository userActivityRepository;

    private UserService userService;

    private ActivityService activityService;

    public UserActivityService(UserActivityRepository userActivityRepository,
        UserService userService, ActivityService activityService) {
        this.userActivityRepository = userActivityRepository;
        this.userService = userService;
        this.activityService = activityService;
    }

    @Transactional(readOnly = true)
    public UserActivityResponses userActivities(Integer page, Integer id) {
        Pageable pageable = PageRequest.of(page - 1, USER_ACTIVITY_PER_PAGE);
        Page<UserActivity> userActivityPage = userActivityRepository.findByUserId(pageable, id);
        List<UserActivity> userActivities = userActivityPage.getContent();
        return new UserActivityResponses(userActivities, userActivityPage,
            score(userActivities));
    }

    public UserActivityResponse create(UserActivityRequest userActivityRequest) {
        User user = userService.findUserById(userActivityRequest.getUserId());
        Activity activity = activityService.findActivityById(userActivityRequest.getActivityId());
        UserActivity userActivity = userActivityRequest.toUserActivity(user, activity);
        userActivityRepository.save(userActivity);
        updateLevel(user, userActivity);
        return new UserActivityResponse(userActivity, user.getLevel().getName());
    }

    public UserActivityResponse update(UserActivityRequest userActivityRequest, Integer id) {
        UserActivity userActivity = findUserActivityById(id);
        User user = userService.findUserById(userActivityRequest.getUserId());
        Activity activity = activityService.findActivityById(userActivityRequest.getActivityId());
        UserActivity requestedUserActivity = userActivityRequest.toUserActivity(user, activity);
        userActivity.update(requestedUserActivity);
        return new UserActivityResponse(userActivity, user.getLevel().getName());
    }

    public String deleteUserActivity(Integer id, Integer userId) {
        UserActivity userActivity = findUserActivityById(id);
        User user = userService.findUserById(userId);
        user.deleteUserActivity(userActivity);
        userActivityRepository.delete(userActivity);
        return user.getLevel().getName();
    }

    private int score(List<UserActivity> userActivities) {
        return userActivities.stream()
            .mapToInt(u -> u.getActivity().getScore())
            .sum();
    }

    private void updateLevel(User user, UserActivity userActivity) {
        if (userActivity.finished()) {
            user.updateLevel(userActivity);
        }
    }

    public UserActivity findUserActivityById(Integer id) {
        return userActivityRepository.findById(id)
            .orElseThrow(() -> new UserActivityNotFoundException(
                "UserActivity not found with id : " + id));
    }

}
