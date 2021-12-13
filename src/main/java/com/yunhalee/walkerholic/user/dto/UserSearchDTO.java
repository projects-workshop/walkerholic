package com.yunhalee.walkerholic.user.dto;

import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSearchDTO {

    private Integer id;

    private String firstname;

    private String lastname;

    private String email;

    private String imageUrl;

    public UserSearchDTO(User user) {
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.imageUrl = user.getImageUrl();
    }
}
