package com.yunhalee.walkerholic.follow.service;

import com.yunhalee.walkerholic.follow.dto.FollowDTO;
import com.yunhalee.walkerholic.follow.domain.Follow;
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

    public FollowDTO follow(Integer fromId, Integer toId) {
        User fromUser = userRepository.findById(fromId).get();
        User toUser = userRepository.findById(toId).get();

        Follow follow = Follow.follow(fromUser, toUser);

        followRepository.save(follow);

        FollowDTO followDTO = new FollowDTO(follow.getId(), follow.getToUser());

        return followDTO;
    }

    public String unfollow(Integer id) {
        followRepository.deleteById(id);
        return "Unfollowed User Successfully.";
    }

    public List<FollowDTO> getFollowers(Integer id) {
        List<Follow> follows = followRepository.findAllByToUserId(id);
        List<FollowDTO> followDTOS = new ArrayList<>();
        follows
            .forEach(follow -> followDTOS.add(new FollowDTO(follow.getId(), follow.getFromUser())));
        return followDTOS;
    }

    public List<FollowDTO> getFollowings(Integer id) {
        List<Follow> follows = followRepository.findAllByFromUserId(id);
        List<FollowDTO> followDTOS = new ArrayList<>();
        follows
            .forEach(follow -> followDTOS.add(new FollowDTO(follow.getId(), follow.getToUser())));
        return followDTOS;
    }

    public HashMap<String, Object> getFollows(Integer id) {
        List<Follow> followers = followRepository.findAllByToUserId(id);
        List<Follow> followings = followRepository.findAllByFromUserId(id);
        List<FollowDTO> followerDTOs = new ArrayList<>();
        followers.forEach(
            follow -> followerDTOs.add(new FollowDTO(follow.getId(), follow.getFromUser())));
        List<FollowDTO> followingDTOS = new ArrayList<>();
        followings.forEach(
            follow -> followingDTOS.add(new FollowDTO(follow.getId(), follow.getToUser())));

        HashMap<String, Object> map = new HashMap<>();
        map.put("followers", followerDTOs);
        map.put("followings", followingDTOS);

        return map;
    }
}
