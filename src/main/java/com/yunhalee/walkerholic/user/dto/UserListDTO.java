package com.yunhalee.walkerholic.user.dto;

import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserListDTO {


    private Integer id;

    private String firstname;

    private String lastname;

    private String email;

    private String role;

    private String imageUrl;

    private String level;

    public UserListDTO(User user) {
        this.id = user.getId();
        this.firstname = user.getFirstname();
        this.lastname = user.getLastname();
        this.email = user.getEmail();
        this.role = user.getRole().name();
        this.imageUrl = user.getImageUrl();
        this.level = user.getLevel().getName();
    }

}
