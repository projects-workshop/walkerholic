package com.yunhalee.walkerholic.follow.controller;

import com.yunhalee.walkerholic.follow.dto.FollowDTO;
import com.yunhalee.walkerholic.follow.service.FollowService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow/{fromId}/{toId}")
    public FollowDTO follow(@PathVariable("fromId") String fromId,
        @PathVariable("toId") String toId) {
        Integer fromUser = Integer.parseInt(fromId);
        Integer toUser = Integer.parseInt(toId);
        return followService.follow(fromUser, toUser);
    }

    @DeleteMapping("/unfollow/{id}")
    public String unfollow(@PathVariable("id") String id) {
        Integer followId = Integer.parseInt(id);
        return followService.unfollow(followId);
    }

    @GetMapping("/followers/{id}")
    public ResponseEntity<?> getFollowers(@PathVariable("id") String id) {
        Integer followId = Integer.parseInt(id);
        return new ResponseEntity<List<FollowDTO>>(followService.getFollowers(followId),
            HttpStatus.OK);
    }

    @GetMapping("/followings/{id}")
    public ResponseEntity<?> getFollowings(@PathVariable("id") String id) {
        Integer followId = Integer.parseInt(id);
        return new ResponseEntity<List<FollowDTO>>(followService.getFollowings(followId),
            HttpStatus.OK);
    }

    @GetMapping("/follows/{id}")
    public ResponseEntity<?> getFollows(@PathVariable("id") String id) {
        System.out.println(id);
        Integer userId = Integer.parseInt(id);
        return new ResponseEntity<HashMap<String, Object>>(followService.getFollows(userId),
            HttpStatus.OK);
    }
}
