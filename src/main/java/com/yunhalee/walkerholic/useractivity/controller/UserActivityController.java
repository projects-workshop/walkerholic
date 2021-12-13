package com.yunhalee.walkerholic.useractivity.controller;

import com.yunhalee.walkerholic.useractivity.dto.UserActivityCreateDTO;
import com.yunhalee.walkerholic.useractivity.service.UserActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class UserActivityController {

    private final UserActivityService userActivityService;

    @GetMapping("/userActivities/{page}/{id}")
    public ResponseEntity<?> getByUser(@PathVariable("page") String page,
        @PathVariable("id") String id) {
        Integer pageNumber = Integer.parseInt(page);
        Integer userId = Integer.parseInt(id);
        return new ResponseEntity<HashMap>(userActivityService.getByUser(pageNumber, userId),
            HttpStatus.OK);
    }

    @PostMapping("/userActivity/save/{id}")
    public ResponseEntity<?> saveUserActivity(
        @RequestBody UserActivityCreateDTO userActivityCreateDTO, @PathVariable("id") String id) {
        Integer userId = Integer.parseInt(id);
        System.out.println(userActivityCreateDTO.getActivityId());
        return new ResponseEntity<HashMap<String, Object>>(
            userActivityService.saveUserActivity(userActivityCreateDTO, userId), HttpStatus.OK);
    }

    @DeleteMapping("/userActivity/delete/{id}/{userId}")
    public ResponseEntity<?> deleteUserActivity(@PathVariable("id") String id,
        @PathVariable("userId") String userId) {
        Integer userActivityId = Integer.parseInt(id);
        Integer userid = Integer.parseInt(userId);
        return new ResponseEntity<String>(
            userActivityService.deleteUserActivity(userActivityId, userid), HttpStatus.OK);
    }


}
