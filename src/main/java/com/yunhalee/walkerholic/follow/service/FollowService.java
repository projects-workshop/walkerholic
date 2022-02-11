package com.yunhalee.walkerholic.follow.service;

import com.yunhalee.walkerholic.follow.dto.FollowResponse;
import com.yunhalee.walkerholic.follow.domain.Follow;
import com.yunhalee.walkerholic.follow.dto.FollowUserResponse;
import com.yunhalee.walkerholic.follow.dto.FollowsResponse;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.follow.domain.FollowRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.user.exception.UserNotFoundException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class FollowService {

    private FollowRepository followRepository;
    private UserRepository userRepository;

    public FollowService(FollowRepository followRepository,
        UserRepository userRepository) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
    }

    public FollowResponse follow(Integer fromId, Integer toId) {
        User fromUser = user(fromId);
        User toUser = user(toId);
        Follow follow = Follow.follow(fromUser, toUser);
        followRepository.save(follow);
        return FollowResponse.of(follow.getId(), FollowUserResponse.of(follow.getToUser()));
    }

    private User user(Integer id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException(
                "User not found with id : " + id));
    }

    public void unfollow(Integer id) {
        followRepository.deleteById(id);
    }

    public List<FollowResponse> getFollowers(Integer id) {
        return followRepository.findAllByToUserId(id).stream()
            .map(follow -> FollowResponse
                .of(follow.getId(), FollowUserResponse.of(follow.getFromUser())))
            .collect(Collectors.toList());
    }

    public List<FollowResponse> getFollowings(Integer id) {
        return followRepository.findAllByFromUserId(id).stream()
            .map(follow -> FollowResponse
                .of(follow.getId(), FollowUserResponse.of(follow.getToUser())))
            .collect(Collectors.toList());
    }

    public FollowsResponse getFollows(Integer id) {
        List<FollowResponse> followers = followRepository.findAllByToUserId(id).stream()
            .map(follow -> FollowResponse
                .of(follow.getId(), FollowUserResponse.of(follow.getFromUser())))
            .collect(Collectors.toList());
        List<FollowResponse> followings = followRepository.findAllByFromUserId(id).stream()
            .map(follow -> FollowResponse
                .of(follow.getId(), FollowUserResponse.of(follow.getToUser())))
            .collect(Collectors.toList());
        return FollowsResponse.of(followers, followings);
    }
}
