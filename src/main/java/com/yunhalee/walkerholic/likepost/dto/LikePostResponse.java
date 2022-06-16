package com.yunhalee.walkerholic.likepost.dto;

import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;

@Getter
public class LikePostResponse {

    private Integer id;

    private Integer userId;

    private String fullname;

    private String imageUrl;

    public LikePostResponse(Integer id, Integer userId, String fullname, String imageUrl) {
        this.id = id;
        this.userId = userId;
        this.fullname = fullname;
        this.imageUrl = imageUrl;
    }

    public static LikePostResponse of(LikePost likePost) {
        User user = likePost.getUser();
        return new LikePostResponse(likePost.getId(), user.getId(), user.getFullName(), user.getImageUrl());
    }
}
