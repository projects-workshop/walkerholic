package com.yunhalee.walkerholic.post.dto;

import com.yunhalee.walkerholic.likepost.dto.LikePostResponse;
import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.user.dto.SimpleUserResponse;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponse {

    private Integer id;

    private String title;

    private String content;

    private SimpleUserResponse user;

    private List<LikePostResponse> postLikes;

    private List<PostImageResponse> postImages;

    private LocalDateTime createdAt;

    public PostResponse() {
    }

    private PostResponse(Post post, List<LikePostResponse> postLikes, List<PostImageResponse> postImages, SimpleUserResponse user) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.user = user;
        this.postLikes = postLikes;
        this.postImages = postImages;
        this.createdAt = post.getCreatedAt();
    }

    public static PostResponse of(Post post, List<LikePostResponse> postLikes, List<PostImageResponse> postImages, SimpleUserResponse user) {
        return new PostResponse(post, postLikes, postImages, user);
    }
}
