package com.yunhalee.walkerholic.likepost.dto;

import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
public class LikePostResponse {

    private Integer id;

    private Integer postId;

    private UserLikePost user;

    private LikePostResponse(Integer id, Integer postId, UserLikePost user) {
        this.id = id;
        this.postId = postId;
        this.user = user;
    }

    public static LikePostResponse of(LikePost likePost) {
        return new LikePostResponse(likePost.getId(), likePost.getPost().getId(), new UserLikePost(likePost.getUser()));
    }

    @Getter
    static class UserLikePost {

        private Integer id;
        private String fullname;
        private String imageUrl;

        public UserLikePost(User user) {
            this.id = user.getId();
            this.fullname = user.getFullname();
            this.imageUrl = user.getImageUrl();
        }
    }
}
