package com.yunhalee.walkerholic.user.dto;

import com.yunhalee.walkerholic.post.domain.Post;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPostDTO {

    private Integer id;

    private String title;

    private String imageUrl;

    private String userImageUrl;

    private String userName;

    private Integer userId;

    public UserPostDTO(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.imageUrl = post.getPostImages().get(0).getFilePath();
        this.userImageUrl = post.getUser().getImageUrl();
        this.userName = post.getUser().getFullname();
        this.userId = post.getUser().getId();
    }

}
