package com.yunhalee.walkerholic.post.dto;

import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class PostRequest {

    private String title;

    private String content;

    private Integer userId;

    public PostRequest(String title, String content, Integer userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public Post toPost(User user) {
        return Post.of(title, content, user);
    }

}
