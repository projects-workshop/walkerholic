package com.yunhalee.walkerholic.useractivity.controller;

import com.yunhalee.walkerholic.useractivity.dto.UserActivityResponses;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityRequest;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityResponse;
import com.yunhalee.walkerholic.useractivity.service.UserActivityService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserActivityController {

    private final UserActivityService userActivityService;

    public UserActivityController(UserActivityService userActivityService) {
        this.userActivityService = userActivityService;
    }

    @GetMapping("/users/{id}/user-activities")
    public ResponseEntity<UserActivityResponses> userActivities(@RequestParam("page") String page, @PathVariable("id") Integer id) {
        return ResponseEntity.ok(userActivityService.userActivities(Integer.parseInt(page), id));
    }

    @PostMapping("/user-activities")
    public ResponseEntity<UserActivityResponse> create(@Valid @RequestBody UserActivityRequest userActivityRequest) {
        return ResponseEntity.ok(userActivityService.create(userActivityRequest));
    }

    @PostMapping("/user-activities/{id}")
    public ResponseEntity<UserActivityResponse> update(@Valid @RequestBody UserActivityRequest userActivityRequest, @PathVariable("id") Integer id) {
        return ResponseEntity.ok(userActivityService.update(userActivityRequest, id));
    }

    @DeleteMapping("/user-activities/{id}/users/{userId}")
    public ResponseEntity<String> deleteUserActivity(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(userActivityService.deleteUserActivity(id, userId));
    }


}
