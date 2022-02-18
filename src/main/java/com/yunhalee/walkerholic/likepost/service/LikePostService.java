package com.yunhalee.walkerholic.likepost.service;

import com.yunhalee.walkerholic.likepost.dto.LikePostResponse;
import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.likepost.dto.LikePostRequest;
import com.yunhalee.walkerholic.post.PostNotFoundException;
import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.likepost.domain.LikePostRepository;
import com.yunhalee.walkerholic.post.domain.PostRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import com.yunhalee.walkerholic.user.exception.UserNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LikePostService {

    private LikePostRepository likePostRepository;

    private PostRepository postRepository;

    private UserRepository userRepository;

    public LikePostService(LikePostRepository likePostRepository, PostRepository postRepository,
        UserRepository userRepository) {
        this.likePostRepository = likePostRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public LikePostResponse likePost(LikePostRequest request) {
        User user = userRepository.findById(request.getUser())
            .orElseThrow(() -> new UserNotFoundException("Post not found with id : " + request.getUser()));
        Post post = postRepository.findById(request.getPost())
            .orElseThrow(() -> new PostNotFoundException("Post not found with id : " + request.getPost()));
        LikePost likePost = likePostRepository.save(LikePost.of(user, post));
        return LikePostResponse.of(likePost);
    }

    public void unlikePost(Integer id) {
        likePostRepository.deleteById(id);
    }
}
