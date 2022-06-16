package com.yunhalee.walkerholic.activity.controller;

import com.yunhalee.walkerholic.activity.dto.ActivityRequest;
import com.yunhalee.walkerholic.activity.dto.ActivityResponse;
import com.yunhalee.walkerholic.activity.dto.ActivityDetailResponse;
import com.yunhalee.walkerholic.activity.service.ActivityService;
import java.io.IOException;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/activities")
public class ActivityController {

    private final ActivityService activityService;

    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @PostMapping
    public ActivityResponse create(@Valid @RequestBody ActivityRequest activityRequest) {
        return activityService.create(activityRequest);
    }

    @PutMapping("/{id}")
    public ActivityResponse update(@PathVariable("id") Integer id,
        @Valid @RequestBody ActivityRequest activityRequest) {
        return activityService.update(id, activityRequest);
    }

    @PostMapping("/images")
    public String uploadImage(@RequestParam("multipartFile") MultipartFile multipartFile)
        throws IOException {
        return activityService.uploadImage(multipartFile);
    }

    @GetMapping("/{id}")
    public ActivityDetailResponse activity(@PathVariable("id") Integer id) {
        return activityService.activity(id);
    }

    @GetMapping
    public List<ActivityResponse> activities() {
        return activityService.activities();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        activityService.delete(id);
    }

}
