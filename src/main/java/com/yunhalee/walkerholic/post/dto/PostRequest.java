package com.yunhalee.walkerholic.post.dto;

import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
public class PostRequest {

    private Integer id;

    private String title;

    private String content;

    private Integer userId;

    public PostRequest(Integer id, String title, String content, Integer userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public PostRequest(String title, String content, Integer userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public Post toPost(User user) {
        return Post.of(title, content, user);
    }

}
