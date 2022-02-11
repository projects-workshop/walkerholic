package com.yunhalee.walkerholic.follow.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class FollowsResponse {

    private List<FollowResponse> followers;
    private List<FollowResponse> followings;

    public FollowsResponse(List<FollowResponse> followers,
        List<FollowResponse> followings) {
        this.followers = followers;
        this.followings = followings;
    }

    public static FollowsResponse of(List<FollowResponse> followers, List<FollowResponse> followings){
        return new FollowsResponse(followers, followings);
    }

}
