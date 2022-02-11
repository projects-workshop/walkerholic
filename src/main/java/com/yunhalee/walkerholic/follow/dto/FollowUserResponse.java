package com.yunhalee.walkerholic.follow.dto;

import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;

@Getter
public class FollowUserResponse {

    private Integer id;
    private String fullname;
    private String imageUrl;

    private FollowUserResponse(User user) {
        this.id = user.getId();
        this.fullname = user.getFullname();
        this.imageUrl = user.getImageUrl();
    }

    public static FollowUserResponse of(User user) {
        return new FollowUserResponse(user);
    }
}
