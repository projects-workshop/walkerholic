package com.yunhalee.walkerholic.user.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponses {

    private List<UserResponse> users;
    private Long totalElement;
    private Integer totalPage;

    private UserResponses(List<UserResponse> users, Long totalElement, Integer totalPage) {
        this.users = users;
        this.totalElement = totalElement;
        this.totalPage = totalPage;
    }

    public static UserResponses of(List<UserResponse> users, Long totalElement, Integer totalPage) {
        return new UserResponses(users, totalElement, totalPage);
    }
}
