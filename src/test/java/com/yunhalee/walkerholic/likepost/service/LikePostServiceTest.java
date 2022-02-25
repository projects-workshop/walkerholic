package com.yunhalee.walkerholic.likepost.service;

import static org.junit.jupiter.api.Assertions.*;

import com.yunhalee.walkerholic.MockBeans;
import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.likepost.dto.LikePostRequest;
import com.yunhalee.walkerholic.likepost.dto.LikePostResponse;
import com.yunhalee.walkerholic.post.domain.PostTest;
import com.yunhalee.walkerholic.user.domain.UserTest;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@ExtendWith(MockitoExtension.class)
@SpringBootTest
@Transactional
class LikePostServiceTest extends MockBeans {

    @InjectMocks
    private LikePostService likePostService;

    private LikePost likePost = new LikePost(1, UserTest.USER, PostTest.POST);


    @DisplayName("게시물에 좋아요를 표시한다.")
    @Test
    void like_post() {
        //when
        when(userService.findUserById(anyInt())).thenReturn(UserTest.USER);
        when(postService.findPostById(anyInt())).thenReturn(PostTest.POST);
        when(likePostRepository.save(any(LikePost.class))).thenReturn(likePost);
        LikePostResponse response = likePostService.likePost(new LikePostRequest(1, 2));

        //then
        assertThat(response.getId()).isNotNull();
        assertThat(response.getUserId()).isEqualTo(UserTest.USER.getId());
    }

    @DisplayName("게시물의 좋아요를 취소한다.")
    @Test
    void unlike_post() {
        //when
        likePostService.unlikePost(1);

        //then
        verify(likePostRepository).deleteById(anyInt());
    }


}