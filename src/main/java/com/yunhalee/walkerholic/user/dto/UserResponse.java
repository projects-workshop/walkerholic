package com.yunhalee.walkerholic.user.dto;

import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@NoArgsConstructor
public class UserResponse {

    private Integer id;
    private String firstname;
    private String lastname;
    private String email;
    private String role;
    private String imageUrl;
    private String phoneNumber;
    private String level;
    private String description;
    private boolean isSeller;

    private UserResponse(User user) {
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.role = user.getRoleName();
        this.imageUrl = user.getImageUrl();
        this.phoneNumber = user.getPhoneNumber();
        this.level = user.getLevelName();
        this.description = user.getDescription();
        this.isSeller = user.isSeller();
    }

    public static UserResponse of(User user) {
        return new UserResponse(user);
    }

}
