package com.yunhalee.walkerholic.follow.service;

import com.yunhalee.walkerholic.follow.dto.FollowResponse;
import com.yunhalee.walkerholic.follow.domain.Follow;
import com.yunhalee.walkerholic.follow.dto.FollowsResponse;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.follow.domain.FollowRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public FollowResponse follow(Integer fromId, Integer toId) {
        User fromUser = userRepository.findById(fromId).get();
        User toUser = userRepository.findById(toId).get();

        Follow follow = Follow.follow(fromUser, toUser);

        followRepository.save(follow);

        FollowResponse followResponse = new FollowResponse(follow.getId(), follow.getToUser());

        return followResponse;
    }

    public String unfollow(Integer id) {
        followRepository.deleteById(id);
        return "Unfollowed User Successfully.";
    }

    public List<FollowResponse> getFollowers(Integer id) {
        List<Follow> follows = followRepository.findAllByToUserId(id);
        List<FollowResponse> followDTOS = new ArrayList<>();
        follows
            .forEach(follow -> followDTOS.add(new FollowResponse(follow.getId(), follow.getFromUser())));
        return followDTOS;
    }

    public List<FollowResponse> getFollowings(Integer id) {
        List<Follow> follows = followRepository.findAllByFromUserId(id);
        List<FollowResponse> followDTOS = new ArrayList<>();
        follows
            .forEach(follow -> followDTOS.add(new FollowResponse(follow.getId(), follow.getToUser())));
        return followDTOS;
    }

    public FollowsResponse getFollows(Integer id) {
        List<Follow> followers = followRepository.findAllByToUserId(id);
        List<Follow> followings = followRepository.findAllByFromUserId(id);
        List<FollowResponse> followerDTOs = new ArrayList<>();
        followers.forEach(
            follow -> followerDTOs.add(new FollowResponse(follow.getId(), follow.getFromUser())));
        List<FollowResponse> followingDTOS = new ArrayList<>();
        followings.forEach(
            follow -> followingDTOS.add(new FollowResponse(follow.getId(), follow.getToUser())));

        HashMap<String, Object> map = new HashMap<>();
        map.put("followers", followerDTOs);
        map.put("followings", followingDTOS);

        return map;
    }
}
