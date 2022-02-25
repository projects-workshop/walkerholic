package com.yunhalee.walkerholic.likepost.service;

import com.yunhalee.walkerholic.likepost.dto.LikePostResponse;
import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.likepost.dto.LikePostRequest;
import com.yunhalee.walkerholic.post.PostNotFoundException;
import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.post.service.PostService;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.likepost.domain.LikePostRepository;
import com.yunhalee.walkerholic.post.domain.PostRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.user.exception.UserNotFoundException;
import com.yunhalee.walkerholic.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LikePostService {

    private LikePostRepository likePostRepository;

    private PostService postService;

    private UserService userService;

    public LikePostService(LikePostRepository likePostRepository, PostService postService,
        UserService userService) {
        this.likePostRepository = likePostRepository;
        this.postService = postService;
        this.userService = userService;
    }

    public LikePostResponse likePost(LikePostRequest request) {
        User user = userService.findUserById(request.getUser());
        Post post = postService.findPostById(request.getPost());
        LikePost likePost = likePostRepository.save(LikePost.of(user, post));
        return LikePostResponse.of(likePost);
    }

    public void unlikePost(Integer id) {
        likePostRepository.deleteById(id);
    }
}
