package com.yunhalee.walkerholic.follow.dto;

import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowResponse {

    private Integer id;

    private followUser user;

    public FollowResponse(Integer id, User user) {
        this.id = id;
        this.user = new followUser(user);
    }

    public static FollowResponse of(Integer id, User user) {
        return new FollowResponse(id, user);
    }

    @Getter
    static class followUser {

        private Integer id;
        private String fullname;
        private String imageUrl;

        public followUser(User user) {
            this.id = user.getId();
            this.fullname = user.getFullname();
            this.imageUrl = user.getImageUrl();
        }
    }

}
