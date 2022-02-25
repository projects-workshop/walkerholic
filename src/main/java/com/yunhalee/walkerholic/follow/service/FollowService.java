package com.yunhalee.walkerholic.follow.service;

import com.yunhalee.walkerholic.follow.dto.FollowResponse;
import com.yunhalee.walkerholic.follow.domain.Follow;
import com.yunhalee.walkerholic.follow.dto.FollowUserResponse;
import com.yunhalee.walkerholic.follow.dto.FollowsResponse;
import com.yunhalee.walkerholic.follow.exception.CannotFollowOneselfException;
import com.yunhalee.walkerholic.follow.exception.FollowAlreadyExistException;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.follow.domain.FollowRepository;
import com.yunhalee.walkerholic.user.service.UserService;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class FollowService {

    private FollowRepository followRepository;
    private UserService userService;

    public FollowService(FollowRepository followRepository, UserService userService) {
        this.followRepository = followRepository;
        this.userService = userService;
    }

    @Transactional
    public FollowResponse follow(Integer fromId, Integer toId) {
        checkFollowValidate(fromId, toId);
        User fromUser = userService.findUserById(fromId);
        User toUser = userService.findUserById(toId);
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

    @Transactional
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
        List<Follow> follows = findAllFollowByToUserId(id);
        return follows.stream()
            .map(follow -> FollowResponse.of(follow.getId(), FollowUserResponse.of(follow.getFromUser())))
            .collect(Collectors.toList());
    }

    private List<FollowResponse> followingsResponses(Integer id) {
        List<Follow> follows = findAllFollowByFromUserId(id);
        return follows.stream()
            .map(follow -> FollowResponse.of(follow.getId(), FollowUserResponse.of(follow.getToUser())))
            .collect(Collectors.toList());
    }

    private List<Follow> findAllFollowByToUserId(Integer id){
        return followRepository.findAllByToUserId(id);
    }

    public List<Follow> findAllFollowByFromUserId(Integer id){
        return followRepository.findAllByFromUserId(id);
    }
}
