package com.yunhalee.walkerholic.useractivity.controller;

import com.yunhalee.walkerholic.useractivity.dto.UserActivityResponses;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityRequest;
import com.yunhalee.walkerholic.useractivity.dto.UserActivityResponse;
import com.yunhalee.walkerholic.useractivity.service.UserActivityService;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-activities")
@RequiredArgsConstructor
public class UserActivityController {

    private final UserActivityService userActivityService;

    @GetMapping("/users/{id}")
    public ResponseEntity<UserActivityResponses> userActivities(@RequestParam("page") String page,
        @PathVariable("id") Integer id) {
        return new ResponseEntity<>(userActivityService.userActivities(Integer.parseInt(page), id),
            HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserActivityResponse> create(
        @Valid @RequestBody UserActivityRequest userActivityRequest) {
        return new ResponseEntity<>(
            userActivityService.create(userActivityRequest), HttpStatus.OK);
    }

    @PostMapping("/{id}")
    public ResponseEntity<UserActivityResponse> update(
        @Valid @RequestBody UserActivityRequest userActivityRequest,
        @PathVariable("id") Integer id) {
        return new ResponseEntity<>(
            userActivityService.update(userActivityRequest, id), HttpStatus.OK);
    }

    @DeleteMapping("/user-activities/{id}/users/{userId}")
    public ResponseEntity<String> deleteUserActivity(@PathVariable("id") Integer id,
        @PathVariable("userId") Integer userId) {
        return new ResponseEntity<>(
            userActivityService.deleteUserActivity(id, userId), HttpStatus.OK);
    }


}
