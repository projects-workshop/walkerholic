package com.yunhalee.walkerholic.follow.service;

import com.yunhalee.walkerholic.follow.dto.FollowDTO;
import com.yunhalee.walkerholic.follow.domain.Follow;
import com.yunhalee.walkerholic.follow.domain.FollowRepository;
import com.yunhalee.walkerholic.follow.service.FollowService;
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
        FollowDTO followDTO = followService.follow(fromUserId, toUserId);

        //then
        assertNotNull(followDTO.getId());
        assertEquals(fromUserId,
            followRepository.findById(followDTO.getId()).get().getFromUser().getId());
        assertEquals(toUserId,
            followRepository.findById(followDTO.getId()).get().getToUser().getId());
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
        HashMap<String, Object> response = followService.getFollows(id);
        List<FollowDTO> followers = (List<FollowDTO>) response.get("followers");
        List<FollowDTO> followings = (List<FollowDTO>) response.get("followins");

        //then
        for (FollowDTO follower : followers) {
            Follow follow = followRepository.findById(follower.getId()).get();
            assertEquals(follow.getToUser().getId(), id);
        }
        for (FollowDTO following : followings) {
            Follow follow = followRepository.findById(following.getId()).get();
            assertEquals(follow.getFromUser().getId(), id);
        }
    }

    @Test
    public void getFollowings() {
        //given
        Integer id = 1;

        //when
        List<FollowDTO> followings = followService.getFollowings(id);

        //then
        for (FollowDTO following : followings) {
            Follow follow = followRepository.findById(following.getId()).get();
            assertEquals(follow.getFromUser().getId(), id);
        }
    }

    @Test
    public void getFollowers() {
        //given
        Integer id = 1;

        //when
        List<FollowDTO> followers = followService.getFollowers(id);

        //then
        for (FollowDTO follower : followers) {
            Follow follow = followRepository.findById(follower.getId()).get();
            assertEquals(follow.getToUser().getId(), id);
        }
    }
}
