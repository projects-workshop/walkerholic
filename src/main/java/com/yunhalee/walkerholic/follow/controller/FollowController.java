package com.yunhalee.walkerholic.follow.controller;

import com.yunhalee.walkerholic.follow.dto.FollowResponse;
import com.yunhalee.walkerholic.follow.dto.FollowsResponse;
import com.yunhalee.walkerholic.follow.service.FollowService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/follows")
    public ResponseEntity<FollowResponse> follow(@RequestParam("fromId") Integer fromId, @RequestParam("toId") Integer toId) {
        return ResponseEntity.ok(followService.follow(fromId, toId));
    }

    @DeleteMapping("/follows/{id}")
    public ResponseEntity unfollow(@PathVariable("id") Integer id) {
        followService.unfollow(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/users/{id}/followers")
    public ResponseEntity<List<FollowResponse>> getFollowers(@PathVariable("id") String id) {
        Integer followId = Integer.parseInt(id);
        return ResponseEntity.ok(followService.getFollowers(followId));
    }

    @GetMapping("/users/{id}/followings")
    public ResponseEntity<List<FollowResponse>> getFollowings(@PathVariable("id") String id) {
        Integer followId = Integer.parseInt(id);
        return ResponseEntity.ok(followService.getFollowings(followId));
    }

    @GetMapping("/follows/{id}")
    public ResponseEntity<FollowsResponse> getFollows(@PathVariable("id") String id) {
        Integer userId = Integer.parseInt(id);
        return ResponseEntity.ok(followService.getFollows(userId));
    }
}
