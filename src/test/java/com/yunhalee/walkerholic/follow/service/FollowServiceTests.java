package com.yunhalee.walkerholic.follow.service;

import com.yunhalee.walkerholic.follow.dto.FollowResponse;
import com.yunhalee.walkerholic.follow.domain.Follow;
import com.yunhalee.walkerholic.follow.domain.FollowRepository;
import com.yunhalee.walkerholic.follow.dto.FollowsResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class FollowServiceTests {

    @Autowired
    FollowService followService;

    @Autowired
    FollowRepository followRepository;

    @Test
    public void follow() {
        //given
        Integer fromUserId = 1;
        Integer toUserId = 2;

        //when
        FollowResponse followResponse = followService.follow(fromUserId, toUserId);

        //then
        assertNotNull(followResponse.getId());
        assertEquals(fromUserId,
            followRepository.findById(followResponse.getId()).get().getFromUser().getId());
        assertEquals(toUserId,
            followRepository.findById(followResponse.getId()).get().getToUser().getId());
    }

    @Test
    public void unfollow() {
        //given
        Integer followId = 1;

        //when
        followService.unfollow(followId);

        //then
        assertNull(followRepository.findById(followId));
    }

    @Test
    public void getFollowByUserId() {
        //given
        Integer id = 1;

        //when
        FollowsResponse response = followService.getFollows(id);
        List<FollowResponse> followers = response.getFollowers();
        List<FollowResponse> followings = response.getFollowings();

        //then
        for (FollowResponse follower : followers) {
            Follow follow = followRepository.findById(follower.getId()).get();
            assertEquals(follow.getToUser().getId(), id);
        }
        for (FollowResponse following : followings) {
            Follow follow = followRepository.findById(following.getId()).get();
            assertEquals(follow.getFromUser().getId(), id);
        }
    }

    @Test
    public void getFollowings() {
        //given
        Integer id = 1;

        //when
        List<FollowResponse> followings = followService.getFollowings(id);

        //then
        for (FollowResponse following : followings) {
            Follow follow = followRepository.findById(following.getId()).get();
            assertEquals(follow.getFromUser().getId(), id);
        }
    }

    @Test
    public void getFollowers() {
        //given
        Integer id = 1;

        //when
        List<FollowResponse> followers = followService.getFollowers(id);

        //then
        for (FollowResponse follower : followers) {
            Follow follow = followRepository.findById(follower.getId()).get();
            assertEquals(follow.getToUser().getId(), id);
        }
    }
}
