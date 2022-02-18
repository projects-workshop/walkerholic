package com.yunhalee.walkerholic.likepost.dto;

public class LikePostRequest {

    private Integer post;
    private Integer user;

    public LikePostRequest(Integer post, Integer user) {
        this.post = post;
        this.user = user;
    }

    public Integer getPost() {
        return post;
    }

    public Integer getUser() {
        return user;
    }
}
