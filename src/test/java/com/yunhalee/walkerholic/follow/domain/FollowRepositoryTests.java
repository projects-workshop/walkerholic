package com.yunhalee.walkerholic.follow.domain;


import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.user.domain.UserTest;
import org.junit.Before;
import org.junit.jupiter.api.DisplayName;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FollowRepositoryTests {

    @Autowired
    FollowRepository followRepository;

    @Autowired
    UserRepository userRepository;

    private Follow follow;
    private User fromUser;
    private User toUser;

    @Before
    public void setUp() {
        fromUser = userRepository.save(UserTest.USER);
        toUser = userRepository.save(UserTest.SELLER);
        follow = Follow.of(fromUser, toUser);
        followRepository.save(follow);
    }

    @DisplayName("팔로잉 아이디로 팔로우 정보를 찾는다.")
    @Test
    public void find_follow_by_to_user_id() {
        //when
        List<Follow> follows = followRepository.findAllByToUserId(toUser.getId());

        //then
        assertThat(follows.size()).isEqualTo(1);
        assertThat(follows.get(0).getFromUser()).isEqualTo(fromUser);
        assertThat(follows.get(0).getToUser()).isEqualTo(toUser);
    }

    @DisplayName("팔로워 아이디로 팔로우 정보를 찾는다.")
    @Test
    public void find_follow_by_from_user_id() {
        //when
        List<Follow> follows = followRepository.findAllByFromUserId(fromUser.getId());

        //then
        assertThat(follows.size()).isEqualTo(1);
        assertThat(follows.get(0).getFromUser()).isEqualTo(fromUser);
        assertThat(follows.get(0).getToUser()).isEqualTo(toUser);
    }


    @DisplayName("사용자 아이디가 포함된 모든 팔로우를 찾는다.")
    @Test
    public void find_follows_by_user_id() {
        //given
        User user = userRepository.save(User.builder()
            .firstname("firstName")
            .lastname("lastName")
            .email("testUser@example.com")
            .password("12345678")
            .role(Role.USER).build());
        followRepository.save(Follow.of(user, fromUser));

        //when
        List<Follow> follows = followRepository.findAllByUserId(fromUser.getId());

        //then
        assertThat(follows.size()).isEqualTo(2);
        follows.forEach(f -> assertThat(hasEqualFromUserOrEqualToUser(f, fromUser)).isTrue());
    }

    @Test
    public void check_exists_of_follow_by_fromId_and_toId() {
        assertThat(followRepository.existsByFromUserAndToUser(fromUser, toUser)).isTrue();
    }

    private boolean hasEqualFromUserOrEqualToUser(Follow follow, User user) {
        return follow.getFromUser().equals(user) || follow.getToUser().equals(user);
    }

}
