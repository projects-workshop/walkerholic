package com.yunhalee.walkerholic.user.dto;

import com.yunhalee.walkerholic.user.domain.Role;
import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String imageUrl;
    private String phoneNumber;
    private String description;
    private boolean isSeller;


    public UserRequest(String firstname, String lastname, String email, String password, String imageUrl, String phoneNumber, String description, boolean isSeller) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.imageUrl = imageUrl;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.isSeller = isSeller;
    }

    public User toUser( String encodedPassword) {
        if (isSeller) {
            return User.builder()
            .firstname(firstname)
            .lastname(lastname)
            .email(email)
            .password(encodedPassword)
            .imageUrl(imageUrl)
            .phoneNumber(phoneNumber)
            .description(description)
            .role(Role.SELLER).build();
        }
        return User.builder()
            .firstname(firstname)
            .lastname(lastname)
            .email(email)
            .password(encodedPassword)
            .imageUrl(imageUrl)
            .phoneNumber(phoneNumber)
            .description(description)
            .role(Role.USER).build();
    }

    public User toUser() {
        if (isSeller) {
            return User.builder()
                .firstname(firstname)
                .lastname(lastname)
                .email(email)
                .imageUrl(imageUrl)
                .phoneNumber(phoneNumber)
                .description(description)
                .role(Role.SELLER).build();
        }
        return User.builder()
            .firstname(firstname)
            .lastname(lastname)
            .email(email)
            .imageUrl(imageUrl)
            .phoneNumber(phoneNumber)
            .description(description)
            .role(Role.USER).build();
    }
}
