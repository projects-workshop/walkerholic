package com.yunhalee.walkerholic.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserTokenResponse {

    private UserResponse user;
    private String token;

    private UserTokenResponse(UserResponse user, String token) {
        this.user = user;
        this.token = token;
    }

    public static UserTokenResponse of(UserResponse user, String token) {
        return new UserTokenResponse(user, token);
    }
}
