package com.yunhalee.walkerholic.user.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSearchResponses {

    private List<UserSearchResponse> users;

    private UserSearchResponses(List<UserSearchResponse> users) {
        this.users = users;
    }

    public static UserSearchResponses of(List<UserSearchResponse> users) {
        return new UserSearchResponses(users);
    }
}
