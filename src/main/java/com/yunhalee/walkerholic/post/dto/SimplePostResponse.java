package com.yunhalee.walkerholic.post.dto;

import com.yunhalee.walkerholic.post.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SimplePostResponse {

    private Integer id;
    private String title;
    private String imageUrl;
    private String userImageUrl;
    private String userName;
    private Integer userId;

    private SimplePostResponse(Integer id, String title, String imageUrl, String userImageUrl, String userName, Integer userId) {
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.userImageUrl = userImageUrl;
        this.userName = userName;
        this.userId = userId;
    }

    public static SimplePostResponse of(Post post) {
        return new SimplePostResponse(
            post.getId(),
            post.getTitle(),
            post.getPostImages().get(0).getFilePath(),
            post.getUser().getImageUrl(),
            post.getUser().getFullname(),
            post.getUser().getId()
        );
    }
}
