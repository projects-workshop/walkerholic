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
    private String phoneNumber;
    private String description;
    private boolean isSeller;


    public UserRequest(String firstname, String lastname, String email, String password, String phoneNumber, String description, boolean isSeller) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.isSeller = isSeller;
    }

    public User toUser(String imageUrl) {
        if (isSeller) {
            return new User(firstname, lastname, email, password, phoneNumber, description, Role.SELLER);
        }
        return new User(firstname, lastname, email, password, phoneNumber, description, Role.USER);
    }
}
