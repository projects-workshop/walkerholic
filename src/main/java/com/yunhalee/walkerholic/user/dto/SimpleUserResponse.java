package com.yunhalee.walkerholic.user.dto;

import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;

@Getter
public class SimpleUserResponse {

    private Integer id;
    private String fullname;
    private String email;
    private String imageUrl;
    private String description;

    private SimpleUserResponse(Integer id, String fullname, String email, String imageUrl, String description) {
        this.id = id;
        this.fullname = fullname;
        this.email = email;
        this.imageUrl = imageUrl;
        this.description = description;
    }

    public static SimpleUserResponse of(User user) {
        return new SimpleUserResponse(user.getId(),
            user.getFullName(),
            user.getEmail(),
            user.getImageUrl(),
            user.getDescription());
    }
}
