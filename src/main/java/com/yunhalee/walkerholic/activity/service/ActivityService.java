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
@Transactional(readOnly = true)
public class ActivityService {

    private ActivityRepository activityRepository;

    private S3ImageUploader s3ImageUploader;

    private static final String UPLOAD_DIR = "activityUploads/";

    public ActivityService(
        ActivityRepository activityRepository,
        S3ImageUploader s3ImageUploader) {
        this.activityRepository = activityRepository;
        this.s3ImageUploader = s3ImageUploader;
    }

    @Transactional
    public ActivityResponse create(ActivityRequest activityRequest,
        MultipartFile multipartFile) throws IOException {
        Activity activity = activityRequest.toActivity();
        activityRepository.save(activity);
        saveImage(activity, multipartFile);

        return new ActivityResponse(activity);
    }

    @Transactional
    public ActivityResponse update(Integer id, ActivityRequest activityRequest,
        MultipartFile multipartFile) throws IOException {
        Activity existingActivity = activityRepository.findById(id)
            .orElseThrow(() -> new ActivityNotFoundException(
                "Activity not found with id : " + id));
        Activity requestActivity = activityRequest.toActivity();
        Activity updatedActivity = existingActivity.update(requestActivity);
        saveImage(updatedActivity, multipartFile);

        return new ActivityResponse(existingActivity);
    }


    public ActivityDetailResponse activity(Integer id) {
        Activity activity = activityRepository.findByActivityId(id);
        ActivityDetailResponse activityDetailResponse = new ActivityDetailResponse(activity);

        return activityDetailResponse;
    }

    public List<ActivityResponse> activities() {
        return activityRepository.findAll().stream().map(ActivityResponse::new)
            .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Integer id) {
        String dir = UPLOAD_DIR + id;
        s3ImageUploader.removeFolder(dir);
        activityRepository.deleteById(id);

        return;
    }


    private void saveImage(Activity activity, MultipartFile multipartFile)
        throws IOException {
        String uploadDir = UPLOAD_DIR + activity.getId();
        String imageUrl = s3ImageUploader
            .saveImageByFolder(uploadDir, multipartFile);
        activity.changeImageUrl(imageUrl);
    }
}
