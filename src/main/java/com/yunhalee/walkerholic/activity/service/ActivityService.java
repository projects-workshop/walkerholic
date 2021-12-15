package com.yunhalee.walkerholic.activity.service;

import com.yunhalee.walkerholic.activity.dto.ActivityRequest;
import com.yunhalee.walkerholic.activity.exception.ActivityNotFoundException;
import com.yunhalee.walkerholic.util.AmazonS3Utils;
import com.yunhalee.walkerholic.activity.dto.ActivityResponse;
import com.yunhalee.walkerholic.activity.dto.ActivityDetailResponse;
import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    private final AmazonS3Utils amazonS3Utils;


    public ActivityResponse create(ActivityRequest activityRequest,
        MultipartFile multipartFile) throws IOException {
        Activity activity = activityRequest.toActivity();
        saveActivityImage(activity, multipartFile);

        activityRepository.save(activity);

        return new ActivityResponse(activity);
    }

    public ActivityResponse update(Integer id, ActivityRequest activityRequest,
        MultipartFile multipartFile) throws IOException {
        Activity existingActivity = activityRepository.findById(id)
            .orElseThrow(() -> new ActivityNotFoundException(
                "Activity not found with id : " + id));
        Activity requestActivity = activityRequest.toActivity();
        Activity updatedActivity = existingActivity.update(requestActivity);
        saveActivityImage(updatedActivity, multipartFile);

        activityRepository.save(updatedActivity);

        return new ActivityResponse(existingActivity);
    }


    public ActivityDetailResponse getActivity(Integer id) {
        Activity activity = activityRepository.findByActivityId(id);
        ActivityDetailResponse activityDetailResponse = new ActivityDetailResponse(activity);

        return activityDetailResponse;
    }

    public List<ActivityResponse> getActivities() {
        List<Activity> activities = activityRepository.findAll();
        List<ActivityResponse> activityResponses = new ArrayList<>();
        activities.forEach(activity -> activityResponses.add(new ActivityResponse(activity)));
        return activityResponses;
    }

    public String deleteActivity(Integer id) {
        String dir = "activityUploads/" + id;
        amazonS3Utils.removeFolder(dir);
        activityRepository.deleteById(id);

        return "Activity Deleted Successfully.";
    }


    private void saveActivityImage(Activity activity, MultipartFile multipartFile)
        throws IOException {
        if (amazonS3Utils.isEmpty(multipartFile)) {
            return;
        }

        boolean isCreated = activity.getId() == null;
        if (isCreated) {
            activityRepository.save(activity);
        }

        String uploadDir = "activityUploads/" + activity.getId();
        String imageUrl = amazonS3Utils
            .saveImageByFolder(uploadDir, multipartFile, isCreated);
        activity.changeImageUrl(imageUrl);
    }
}
