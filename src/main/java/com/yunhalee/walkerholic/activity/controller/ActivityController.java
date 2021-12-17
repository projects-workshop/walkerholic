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
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping("/activities")
    public ActivityResponse create(@RequestParam("name") String name,
        @RequestParam("score") Integer score,
        @RequestParam("description") String description,
        @RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile)
        throws IOException {
        ActivityRequest activityRequest = new ActivityRequest(name, score, description);
        return activityService.create(activityRequest, multipartFile);
    }

    @PutMapping("/activities/{id}")
    public ActivityResponse update(@PathVariable("id") Integer id,
        @RequestParam("name") String name,
        @RequestParam("score") Integer score,
        @RequestParam("description") String description,
        @RequestParam(value = "multipartFile", required = false) MultipartFile multipartFile)
        throws IOException {
        ActivityRequest activityRequest = new ActivityRequest(name, score, description);
        return activityService.update(id, activityRequest, multipartFile);
    }

    @GetMapping("/activities/{id}")
    public ActivityDetailResponse activity(@PathVariable("id") Integer id) {
        return activityService.activity(id);
    }

    @GetMapping("/activities")
    public List<ActivityResponse> activities() {
        return activityService.activities();
    }

    @DeleteMapping("/activities/{id}")
    public void delete(@PathVariable("id") Integer id) {
        activityService.delete(id);
    }

}
