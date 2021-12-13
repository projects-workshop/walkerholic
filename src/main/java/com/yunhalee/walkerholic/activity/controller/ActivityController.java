package com.yunhalee.walkerholic.activity.controller;

import com.yunhalee.walkerholic.activity.dto.ActivityCreateDTO;
import com.yunhalee.walkerholic.activity.dto.ActivityDTO;
import com.yunhalee.walkerholic.activity.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

  @PostMapping("/activity/save")
    public ActivityCreateDTO saveActivity(@RequestParam(value = "id", required = false) Integer id,
        @RequestParam("name") String name,
        @RequestParam("score") Integer score,
        @RequestParam("description") String description,
        @RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile) {

        ActivityCreateDTO activityCreateDTO = new ActivityCreateDTO(id, name, score, description);
        return activityService.saveActivity(activityCreateDTO, multipartFile);
    }

    @GetMapping("/activity/{id}")
    public ActivityDTO getActivity(@PathVariable("id") String id) {
        Integer activityId = Integer.parseInt(id);
        return activityService.getActivity(activityId);
    }

    @GetMapping("/activities")
    public List<ActivityCreateDTO> getActivities() {
        return activityService.getActivities();
    }

    @DeleteMapping("/deleteActivity/{id}")
    public String deleteActivity(@PathVariable("id") String id) {
        Integer activityId = Integer.parseInt(id);
        return activityService.deleteActivity(activityId);
    }

}
