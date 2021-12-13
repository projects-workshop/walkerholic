package com.yunhalee.walkerholic.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterDTO {

    private Integer id;

    private String firstname;

    private String lastname;

    private String email;

    private String password;

    private String phoneNumber;

    private String description;

    private boolean isSeller;

    public UserRegisterDTO(Integer id, String firstname, String lastname, String email,
        String password, String phoneNumber, String description, boolean isSeller) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.isSeller = isSeller;
    }

    public UserRegisterDTO(String firstname, String lastname, String email, String password,
        String phoneNumber, String description, boolean isSeller) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.isSeller = isSeller;
    }
}
