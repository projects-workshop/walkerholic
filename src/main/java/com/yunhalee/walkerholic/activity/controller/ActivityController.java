package com.yunhalee.walkerholic.activity.controller;

import com.yunhalee.walkerholic.activity.dto.ActivityRequest;
import com.yunhalee.walkerholic.activity.dto.ActivityResponse;
import com.yunhalee.walkerholic.activity.dto.ActivityDetailResponse;
import com.yunhalee.walkerholic.activity.service.ActivityService;
import java.io.IOException;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ActivityResponse> create(@Valid @RequestBody ActivityRequest activityRequest) {
        return ResponseEntity.ok(activityService.create(activityRequest));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActivityResponse> update(@PathVariable("id") Integer id, @Valid @RequestBody ActivityRequest activityRequest) {
        return ResponseEntity.ok(activityService.update(id, activityRequest));
    }

    @PostMapping("/images")
    public ResponseEntity<String> uploadImage(@RequestParam("multipartFile") MultipartFile multipartFile) {
        return ResponseEntity.ok(activityService.uploadImage(multipartFile));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ActivityDetailResponse> activity(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(activityService.activity(id));
    }

    @GetMapping
    public ResponseEntity<List<ActivityResponse>> activities() {
        return ResponseEntity.ok(activityService.activities());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        activityService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
