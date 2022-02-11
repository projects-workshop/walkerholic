package com.yunhalee.walkerholic.follow.service;

import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.follow.domain.FollowTest;
import com.yunhalee.walkerholic.follow.dto.FollowResponse;
import com.yunhalee.walkerholic.follow.domain.Follow;
import com.yunhalee.walkerholic.follow.dto.FollowsResponse;
import com.yunhalee.walkerholic.user.domain.UserTest;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional
class FollowServiceTests extends MockBeans {

    @InjectMocks
    private FollowService followService;

    @Test
    void follow() {
        //given
        Integer fromUserId = 1;
        Integer toUserId = 2;
        when(userRepository.findById(fromUserId)).thenReturn(java.util.Optional.of(UserTest.USER));
        when(userRepository.findById(toUserId)).thenReturn(java.util.Optional.of(UserTest.SELLER));

        //when
        FollowResponse response = followService.follow(fromUserId, toUserId);

        //then
        assertThat(response.getUser().getFullname()).isEqualTo(UserTest.SELLER.getFullname());
    }

    @Test
    public void unfollow() {
        //when
        followService.unfollow(1);

        //then
        verify(followRepository).deleteById(anyInt());
    }

    @Test
    public void find_follows_by_user_id() {
        //given
        List<Follow> follows = new ArrayList<>();
        follows.add(FollowTest.FOLLOW);
        when(followRepository.findAllByFromUserId(anyInt())).thenReturn(follows);

        //when
        FollowsResponse response = followService.getFollows(1);
        List<FollowResponse> followings = response.getFollowings();

        //then
        assertThat(followings.get(0).getUser().getFullname()).isEqualTo(UserTest.SELLER.getFullname());
    }

    @Test
    public void find_followings_by_user_id() {
        //given
        List<Follow> follows = new ArrayList<>();
        follows.add(FollowTest.FOLLOW);
        when(followRepository.findAllByFromUserId(anyInt())).thenReturn(follows);

        //when
        List<FollowResponse> followings = followService.getFollowings(1);

        //then
        assertThat(followings.get(0).getUser().getFullname()).isEqualTo(UserTest.SELLER.getFullname());
    }

    @Test
    public void find_followers_by_user_id() {
        //given
        List<Follow> follows = new ArrayList<>();
        follows.add(FollowTest.FOLLOW);
        when(followRepository.findAllByToUserId(anyInt())).thenReturn(follows);

        //when
        List<FollowResponse> followers = followService.getFollowers(1);

        //then
        assertThat(followers.get(0).getUser().getFullname()).isEqualTo(UserTest.USER.getFullname());
    }
}
