package com.yunhalee.walkerholic.user.dto;

import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserIconResponse {

    private Integer id;
    private String fullname;
    private String imageUrl;

    private UserIconResponse(User user) {
        this.id = user.getId();
        this.fullname = user.getFullName();
        this.imageUrl = user.getImageUrl();
    }

    public static UserIconResponse of(User user){
        return new UserIconResponse(user);
    }

}
