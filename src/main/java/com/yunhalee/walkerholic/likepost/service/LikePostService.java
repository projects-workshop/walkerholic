package com.yunhalee.walkerholic.likepost.service;

import com.yunhalee.walkerholic.likepost.dto.LikePostDTO;
import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.user.domain.User;
import com.yunhalee.walkerholic.likepost.domain.LikePostRepository;
import com.yunhalee.walkerholic.post.domain.PostRepository;
import com.yunhalee.walkerholic.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikePostService {

    private final LikePostRepository likePostRepository;

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    public LikePostDTO likePost(Integer postId, Integer userId) {
        Post post = postRepository.findById(postId).get();
        User user = userRepository.findById(userId).get();

        LikePost likePost = LikePost.likePost(post, user);
        likePostRepository.save(likePost);
        return new LikePostDTO(likePost);
    }

    public Integer unlikePost(Integer id) {
        likePostRepository.deleteById(id);
        return id;
    }
}
