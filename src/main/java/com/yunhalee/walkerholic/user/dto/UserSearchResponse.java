package com.yunhalee.walkerholic.user.dto;

import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UserSearchResponse {

    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String imageUrl;

    private UserSearchResponse(User user) {
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.imageUrl = user.getImageUrl();
    }

    public static UserSearchResponse of(User user) {
        return new UserSearchResponse(user);
    }
}
