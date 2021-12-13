package com.yunhalee.walkerholic.follow.dto;

import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FollowDTO {

    private Integer id;

    private followUser user;

    public FollowDTO(Integer id, User user) {
        this.id = id;
        this.user = new followUser(user);
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
