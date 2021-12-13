package com.yunhalee.walkerholic.useractivity.service;

import com.yunhalee.walkerholic.useractivity.dto.UserActivityCreateDTO;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityDTO;
import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.activity.domain.ActivityStatus;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
import com.yunhalee.walkerholic.useractivity.domain.UserActivityRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserActivityService {

    private final UserActivityRepository userActivityRepository;

    private final UserRepository userRepository;

    private final ActivityRepository activityRepository;

    public static final int USERACTIVITY_PER_PAGE = 10;

    public HashMap<String, Object> getByUser(Integer page, Integer id) {
        Pageable pageable = PageRequest.of(page - 1, USERACTIVITY_PER_PAGE);
        Page<UserActivity> userActivityPage = userActivityRepository.findByUserId(pageable, id);
        List<UserActivity> userActivities = userActivityPage.getContent();
        List<UserActivityDTO> userActivityDTOS = new ArrayList<>();
        Integer score = 0;

        for (UserActivity userActivity : userActivities) {
            userActivityDTOS.add(new UserActivityDTO(userActivity,
                userActivity.getStatus() == ActivityStatus.FINISHED));
            if (userActivity.getStatus() == ActivityStatus.FINISHED) {
                score += userActivity.getActivity().getScore();
            }
        }

        HashMap<String, Object> userActivityList = new HashMap<>();
        userActivityList.put("activities", userActivityDTOS);
        userActivityList.put("totalPage", userActivityPage.getTotalPages());
        userActivityList.put("totalElement", userActivityPage.getTotalElements());
        userActivityList.put("score", score);

        return userActivityList;
    }

    public HashMap<String, Object> saveUserActivity(UserActivityCreateDTO userActivityCreateDTO,
        Integer id) {

        HashMap<String, Object> map = new HashMap<>();

        if (userActivityCreateDTO.getId() != null) {
            UserActivity existingUserActivity = userActivityRepository
                .findById(userActivityCreateDTO.getId()).get();

            if (userActivityCreateDTO.isFinished()) {
                if (existingUserActivity.getStatus() != ActivityStatus.FINISHED) {
                    existingUserActivity.setStatus(ActivityStatus.FINISHED);
                    User user = userRepository.findById(id).get();
                    user.addUserActivity(existingUserActivity);
                    userRepository.save(user);
                    map.put("level", user.getLevel().getName());
                }
            } else {
                existingUserActivity.setStatus(ActivityStatus.ONGOING);
            }
            userActivityRepository.save(existingUserActivity);
            UserActivityDTO userActivityDTO = new UserActivityDTO(existingUserActivity,
                existingUserActivity.getStatus() == ActivityStatus.FINISHED);
            map.put("activity", userActivityDTO);
        } else {
            UserActivity userActivity = new UserActivity();
            User user = userRepository.findById(id).get();
            Activity activity = activityRepository.findById(userActivityCreateDTO.getActivityId())
                .get();

            userActivity.setUser(user);
            userActivity.setActivity(activity);
            System.out.println(userActivityCreateDTO.isFinished());
            if (userActivityCreateDTO.isFinished()) {
                userActivity.setStatus(ActivityStatus.FINISHED);
                user.addUserActivity(userActivity);
                userRepository.save(user);
            } else {
                userActivity.setStatus(ActivityStatus.ONGOING);
            }

            userActivityRepository.save(userActivity);
            UserActivityDTO userActivityDTO = new UserActivityDTO(userActivity,
                userActivity.getStatus() == ActivityStatus.FINISHED);
            map.put("activity", userActivityDTO);
            map.put("level", user.getLevel().getName());
        }
        return map;
    }

    public String deleteUserActivity(Integer id, Integer userId) {
        UserActivity userActivity = userActivityRepository.findById(id).get();
        userActivityRepository.delete(userActivity);

        User user = userRepository.findById(userId).get();
        user.deleteUserActivity();
        userRepository.save(user);

        String level = user.getLevel().getName();

        return level;
    }
}
