package com.yunhalee.walkerholic.useractivity.service;

import com.yunhalee.walkerholic.activity.exception.ActivityNotFoundException;
import com.yunhalee.walkerholic.user.exception.UserNotFoundException;
import com.yunhalee.walkerholic.useractivity.domain.ActivityStatus;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityResponses;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityRequest;
import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
import com.yunhalee.walkerholic.useractivity.domain.UserActivityRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
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

    private UserActivityRepository userActivityRepository;

    private UserRepository userRepository;

    private ActivityRepository activityRepository;

    private static final int USER_ACTIVITY_PER_PAGE = 10;

    public UserActivityService(UserActivityRepository userActivityRepository,
        UserRepository userRepository, ActivityRepository activityRepository) {
        this.userActivityRepository = userActivityRepository;
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
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
        User user = user(userActivityRequest.getUserId());
        Activity activity = activity(userActivityRequest.getActivityId());
        UserActivity userActivity = userActivityRequest.toUserActivity(user, activity);
        userActivityRepository.save(userActivity);
        updateLevel(user, userActivity);
        return new UserActivityResponse(userActivity, user.getLevel().getName());
    }

    public UserActivityResponse update(UserActivityRequest userActivityRequest, Integer id) {
        UserActivity userActivity = userActivity(id);
        User user = user(userActivityRequest.getUserId());
        Activity activity = activity(userActivityRequest.getActivityId());
        UserActivity requestedUserActivity = userActivityRequest.toUserActivity(user, activity);
        userActivity.update(requestedUserActivity);
        return new UserActivityResponse(userActivity, user.getLevel().getName());
    }

    public String deleteUserActivity(Integer id, Integer userId) {
        UserActivity userActivity = userActivity(id);
        userActivityRepository.delete(userActivity);
        User user = user(userId);
        user.deleteUserActivity();
        return user.getLevel().getName();
    }

    private int score(List<UserActivity> userActivities) {
        return userActivities.stream()
            .mapToInt(u -> u.getActivity().getScore())
            .sum();
    }

    private void updateLevel(User user, UserActivity userActivity) {
        if (userActivity.getStatus() == ActivityStatus.FINISHED) {
            user.addUserActivity(userActivity);
        }
    }

    private UserActivity userActivity(Integer id) {
        return userActivityRepository.findById(id)
            .orElseThrow(() -> new UserActivityNotFoundException(
                "UserActivity not found with id : " + id));
    }

    private User user(Integer id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(
                "User not found with id : " + id));
    }

    private Activity activity(Integer id) {
        return activityRepository.findById(id)
            .orElseThrow(() -> new ActivityNotFoundException(
                "Activity not found with id : " + id));
    }


}
