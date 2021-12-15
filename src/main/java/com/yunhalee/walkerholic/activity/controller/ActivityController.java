package com.yunhalee.walkerholic.activity.controller;

import com.yunhalee.walkerholic.activity.dto.ActivityRequest;
import com.yunhalee.walkerholic.activity.dto.ActivityResponse;
import com.yunhalee.walkerholic.activity.dto.ActivityDetailResponse;
import com.yunhalee.walkerholic.activity.service.ActivityService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ActivityController {

    private final ActivityService activityService;

    @PostMapping("/activity")
    public ActivityResponse create(@RequestParam("name") String name,
        @RequestParam("score") Integer score,
        @RequestParam("description") String description,
        @RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile)
        throws IOException {
        ActivityRequest activityRequest = new ActivityRequest(name, score, description);
        return activityService.create(activityRequest, multipartFile);
    }

    @PutMapping("/activity/{id}")
    public ActivityResponse update(@PathVariable("id") Integer id,
        @RequestParam("name") String name,
        @RequestParam("score") Integer score,
        @RequestParam("description") String description,
        @RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile)
        throws IOException {
        ActivityRequest activityRequest = new ActivityRequest(name, score, description);
        return activityService.update(id, activityRequest, multipartFile);
    }

    @GetMapping("/activity/{id}")
    public ActivityDetailResponse getActivity(@PathVariable("id") Integer id) {
        return activityService.getActivity(id);
    }

    @GetMapping("/activities")
    public List<ActivityResponse> getActivities() {
        return activityService.getActivities();
    }

    @DeleteMapping("/activity/{id}")
    public String delete(@PathVariable("id") Integer id) {
        return activityService.deleteActivity(id);
    }

}
