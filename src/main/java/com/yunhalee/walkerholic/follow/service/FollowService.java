package com.yunhalee.walkerholic.follow.service;

import com.yunhalee.walkerholic.follow.dto.FollowResponse;
import com.yunhalee.walkerholic.follow.domain.Follow;
import com.yunhalee.walkerholic.follow.dto.FollowUserResponse;
import com.yunhalee.walkerholic.follow.dto.FollowsResponse;
import com.yunhalee.walkerholic.follow.exception.CannotFollowOneselfException;
import com.yunhalee.walkerholic.follow.exception.FollowAlreadyExistException;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.follow.domain.FollowRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.user.exception.UserNotFoundException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

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
        checkFollowValidate(fromId, toId);
        User fromUser = user(fromId);
        User toUser = user(toId);
        Follow follow = Follow.of(fromUser, toUser);
        followRepository.save(follow);
        return FollowResponse.of(follow.getId(), FollowUserResponse.of(follow.getToUser()));
    }

    private void checkFollowValidate(Integer fromId, Integer toId) {
        if (fromId.equals(toId)) {
            throw new CannotFollowOneselfException("User cannot follow oneself.");
        }
        if (followRepository.existsByFromUserIdAndToUserId(fromId, toId)) {
            throw new FollowAlreadyExistException("user : " + fromId + "already followed user : " + toId);
        }
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
        return followersResponses(id);
    }

    public List<FollowResponse> getFollowings(Integer id) {
        return followingsResponses(id);
    }

    public FollowsResponse getFollows(Integer id) {
        List<FollowResponse> followers = followersResponses(id);
        List<FollowResponse> followings = followingsResponses(id);
        return FollowsResponse.of(followers, followings);
    }

    private List<FollowResponse> followersResponses(Integer id) {
        List<Follow> follows = followRepository.findAllByToUserId(id);
        return follows.stream()
            .map(follow -> FollowResponse.of(follow.getId(), FollowUserResponse.of(follow.getFromUser())))
            .collect(Collectors.toList());
    }

    private List<FollowResponse> followingsResponses(Integer id) {
        List<Follow> follows = followRepository.findAllByFromUserId(id);
        return follows.stream()
            .map(follow -> FollowResponse.of(follow.getId(), FollowUserResponse.of(follow.getToUser())))
            .collect(Collectors.toList());
    }
}
