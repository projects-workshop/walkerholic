package com.yunhalee.walkerholic.activity.service;

import com.yunhalee.walkerholic.activity.dto.ActivityRequest;
import com.yunhalee.walkerholic.activity.exception.ActivityNotFoundException;
import com.yunhalee.walkerholic.common.service.S3ImageUploader;
import com.yunhalee.walkerholic.activity.dto.ActivityResponse;
import com.yunhalee.walkerholic.activity.dto.ActivityDetailResponse;
import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
import java.io.IOException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
public class ActivityService {

    private static final String UPLOAD_DIR = "activity-uploads";

    private ActivityRepository activityRepository;

    private S3ImageUploader s3ImageUploader;

    public ActivityService(
        ActivityRepository activityRepository, S3ImageUploader s3ImageUploader) {
        this.activityRepository = activityRepository;
        this.s3ImageUploader = s3ImageUploader;
    }

    public ActivityResponse create(ActivityRequest activityRequest) {
        Activity activity = activityRequest.toActivity();
        activity.changeImageUrl(activityRequest.getImageUrl());
        activityRepository.save(activity);
        return new ActivityResponse(activity);
    }

    public ActivityResponse update(Integer id, ActivityRequest activityRequest) {
        Activity existingActivity = findActivityById(id);
        Activity requestActivity = activityRequest.toActivity();
        s3ImageUploader.deleteOriginalImage(existingActivity.getImageUrl(), activityRequest.getImageUrl());
        Activity updatedActivity = existingActivity.update(requestActivity);
        return new ActivityResponse(updatedActivity);
    }

    @Transactional(readOnly = true)
    public ActivityDetailResponse activity(Integer id) {
        Activity activity = activityRepository.findByActivityId(id);
        ActivityDetailResponse activityDetailResponse = new ActivityDetailResponse(activity);

        return activityDetailResponse;
    }

    @Transactional(readOnly = true)
    public List<ActivityResponse> activities() {
        return activityRepository.findAll().stream()
            .map(ActivityResponse::new)
            .collect(Collectors.toList());
    }

    public void delete(Integer id) {
        Activity activity = findActivityById(id);
        s3ImageUploader.deleteFile(activity.getImageUrl());
        activityRepository.delete(activity);
        return;
    }

    public String uploadImage(MultipartFile multipartFile) {
        String imageUrl = s3ImageUploader.uploadImage(UPLOAD_DIR, multipartFile);
        return imageUrl;
    }

    public Activity findActivityById(Integer id){
        return activityRepository.findById(id)
            .orElseThrow(() -> new ActivityNotFoundException("Activity not found with id : " + id));
    }


}
