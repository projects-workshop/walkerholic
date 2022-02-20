package com.yunhalee.walkerholic.post.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class UserPostResponse {

    private List<SimplePostResponse> posts;
    private List<SimplePostResponse> likePosts;

    public UserPostResponse(List<SimplePostResponse> posts, List<SimplePostResponse> likePosts) {
        this.posts = posts;
        this.likePosts = likePosts;
    }
}
