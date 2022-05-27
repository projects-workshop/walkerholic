package com.yunhalee.walkerholic.user.dto;

import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class SellerUserResponse {

    private Integer id;

    private String fullname;

    private String email;

    private String imageUrl;

    private String phoneNumber;

    private String level;

    private String description;

    public SellerUserResponse(User user) {
        this.id = user.getId();
        this.fullname = user.getFullname();
        this.email = user.getEmail();
        this.imageUrl = user.getImageUrl();
        this.phoneNumber = user.getPhoneNumber();
        this.level = user.getLevel().getName();
        this.description = user.getDescription();
    }

    public static SellerUserResponse of(User user){
        return new SellerUserResponse(user);
    }
}
