package com.yunhalee.walkerholic.activity.service;

import com.yunhalee.walkerholic.util.FileUploadUtils;
import com.yunhalee.walkerholic.activity.dto.ActivityCreateDTO;
import com.yunhalee.walkerholic.activity.dto.ActivityDTO;
import com.yunhalee.walkerholic.activity.domain.Activity;
import com.yunhalee.walkerholic.activity.domain.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    
    private void saveActivityImage(Activity activity, MultipartFile multipartFile, boolean isNew) {
        try {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            String uploadDir = "activityUploads/" + activity.getId();

            if (!isNew) {
                FileUploadUtils.cleanDir(uploadDir);
            }

            FileUploadUtils.saveFile(uploadDir, fileName, multipartFile);
            activity.setImageUrl("/activityUploads/" + activity.getId() + "/" + fileName);

            activityRepository.save(activity);

        } catch (IOException ex) {
            new IOException("Could not save file : " + multipartFile.getOriginalFilename());
        }
    }

    public ActivityCreateDTO saveActivity(ActivityCreateDTO activityCreateDTO,
        MultipartFile multipartFile) {

        if (activityCreateDTO.getId() != null) {
            Activity existingActivity = activityRepository.findById(activityCreateDTO.getId())
                .get();
            existingActivity.setName(activityCreateDTO.getName());
            existingActivity.setDescription(activityCreateDTO.getDescription());
            existingActivity.setScore(activityCreateDTO.getScore());

            if (multipartFile != null) {
                saveActivityImage(existingActivity, multipartFile, false);
            }
            activityRepository.save(existingActivity);
            return new ActivityCreateDTO(existingActivity);
        } else {
            Activity activity = new Activity();
            activity.setName(activityCreateDTO.getName());
            activity.setDescription(activityCreateDTO.getDescription());
            activity.setScore(activityCreateDTO.getScore());
            activityRepository.save(activity);

            if (multipartFile != null) {
                saveActivityImage(activity, multipartFile, true);
            }
            activityRepository.save(activity);
            return new ActivityCreateDTO(activity);
        }


    }



    public ActivityDTO getActivity(Integer id) {
        Activity activity = activityRepository.findByActivityId(id);
        ActivityDTO activityDTO = new ActivityDTO(activity);

        return activityDTO;
    }

    public List<ActivityCreateDTO> getActivities() {
        List<Activity> activities = activityRepository.findAll();
        List<ActivityCreateDTO> activityCreateDTOS = new ArrayList<>();
        activities.forEach(activity -> activityCreateDTOS.add(new ActivityCreateDTO(activity)));
        return activityCreateDTOS;
    }

    public String deleteActivity(Integer id) {
        String dir = "/activityUploads/" + id;
        FileUploadUtils.deleteDir(dir);
        activityRepository.deleteById(id);
        return "Activity Deleted Successfully.";
    }

}
