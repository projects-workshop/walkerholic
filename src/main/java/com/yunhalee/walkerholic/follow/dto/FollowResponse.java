package com.yunhalee.walkerholic.follow.dto;

import lombok.Getter;

@Getter
public class FollowResponse {

    private Integer id;

    private FollowUserResponse user;

    private FollowResponse(Integer id, FollowUserResponse user) {
        this.id = id;
        this.user = user;
    }

    public static FollowResponse of(Integer id, FollowUserResponse user) {
        return new FollowResponse(id, user);
    }

}
