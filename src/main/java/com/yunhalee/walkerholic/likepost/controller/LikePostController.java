package com.yunhalee.walkerholic.likepost.controller;

import com.yunhalee.walkerholic.likepost.dto.LikePostDTO;
import com.yunhalee.walkerholic.likepost.service.LikePostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikePostController {

    private final LikePostService likePostService;

    @PostMapping("/likePost/{postId}/{userId}")
    public LikePostDTO likePost(@PathVariable("postId") String postId,
        @PathVariable("userId") String userId) {
        Integer post = Integer.parseInt(postId);
        Integer user = Integer.parseInt(userId);
        return likePostService.likePost(post, user);
    }

    @DeleteMapping("/unlikePost/{id}")
    public Integer unlikePost(@PathVariable("id") String id) {
        Integer likePostId = Integer.parseInt(id);
        return likePostService.unlikePost(likePostId);
    }
}
