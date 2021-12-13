package com.yunhalee.walkerholic.follow.domain;


import com.yunhalee.walkerholic.follow.domain.Follow;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.follow.domain.FollowRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@Rollback(false)
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
public class FollowRepostioryTests {

    @Autowired
    FollowRepository followRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void getFollowByUserId() {
        //given
        Integer id = 1;

        //when
        List<Follow> follows = followRepository.findAllByUserId(id);

        //then
        System.out.println(follows);
    }

    @Test
    public void getFollowByToUserId() {
        //given
        Integer id = 1;

        //when
        List<Follow> follows = followRepository.findAllByToUserId(id);

        //then
        for (Follow follow : follows) {
            Integer toUserId = follow.getToUser().getId();
            assertThat(toUserId).isEqualTo(id);
        }
    }

    @Test
    public void getFollowByFromUserId() {
        //given
        Integer id = 1;

        //when
        List<Follow> follows = followRepository.findAllByFromUserId(id);

        //then
        for (Follow follow : follows) {
            Integer fromUserId = follow.getFromUser().getId();
            assertThat(fromUserId).isEqualTo(id);
        }
    }

    @Test
    public void createFollow() {
        //given
        Integer fromUserId = 1;
        Integer toUserId = 2;
        User fromUser = userRepository.findById(fromUserId).get();
        User toUser = userRepository.findById(toUserId).get();

        //when
        Follow follow = Follow.follow(fromUser, toUser);
        Follow follow1 = followRepository.save(follow);

        //then
        assertThat(follow1.getId()).isNotNull();
        assertThat(follow1.getFromUser().getId()).isEqualTo(fromUserId);
        assertThat(follow1.getToUser().getId()).isEqualTo(toUserId);
    }

    @Test
    public void deleteFollow() {
        //given
        Integer followId = 1;

        //when
        followRepository.deleteById(followId);

        //then
        assertThat(followRepository.findById(followId)).isNull();
    }

}
