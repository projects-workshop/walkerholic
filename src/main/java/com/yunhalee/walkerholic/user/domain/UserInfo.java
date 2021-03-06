package com.yunhalee.walkerholic.user.domain;

import com.yunhalee.walkerholic.security.oauth.domain.OAuth2UserInfo;
import com.yunhalee.walkerholic.security.oauth.domain.ProviderType;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import java.util.Arrays;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Embeddable
@Getter
@NoArgsConstructor
public class UserInfo {

    private static final String DEFAULT_IMAGE_URL = "https://walkerholic-with-you.s3.ap-northeast-2.amazonaws.com/globe-asia-solid.svg";

    @Column(name = "firstname", nullable = false, length = 45)
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 45)
    private String lastname;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "image_url")
    private String imageUrl = DEFAULT_IMAGE_URL;

    @Column(length = 13)
    private String phoneNumber;

    private String description;

    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType = NotificationType.NONE;

    @Builder
    public UserInfo(@NonNull String firstname, @NonNull String lastname, @NonNull Role role, String imageUrl,
        String phoneNumber, String description,
        NotificationType notificationType) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.role = role;
        this.imageUrl = imageUrl;
        this.phoneNumber = phoneNumber;
        this.description = description;
        this.notificationType = notificationType;
    }

    public String getFullname() {
        return this.firstname + this.lastname;
    }

    public boolean isSeller() {
        return this.role == Role.SELLER;
    }

    public String getRoleName() {
        return this.role.name();
    }

    public void update(User toUser) {
        this.firstname = toUser.getFirstname();
        this.lastname = toUser.getLastname();
        this.role = toUser.getRole();
        this.imageUrl = toUser.getImageUrl();
        this.phoneNumber = toUser.getPhoneNumber();
        this.description = toUser.getDescription();
    }

    public boolean isDefaultImageUrl() {
        return this.imageUrl.equals(DEFAULT_IMAGE_URL);
    }

    public void updateOAuth2User(OAuth2UserInfo userInfo) {
        updateFirstName(userInfo.getFirstName());
        updateLastName(userInfo.getLastName());
        updateImageUrl(userInfo.getImageUrl());
    }

    private void updateFirstName(String firstname) {
        if (!firstname.isEmpty() || !firstname.isBlank()) {
            this.firstname = firstname;
        }
    }

    private void updateLastName(String lastname) {
        if (!lastname.isEmpty() || !lastname.isBlank()) {
            this.lastname = lastname;
        }
    }

    private void updateImageUrl(String imageUrl) {
        if (!imageUrl.isEmpty() || !imageUrl.isBlank()) {
            this.imageUrl = imageUrl;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfo userInfo = (UserInfo) o;
        return Objects.equals(firstname, userInfo.firstname) && Objects
            .equals(lastname, userInfo.lastname) && role == userInfo.role && Objects
            .equals(imageUrl, userInfo.imageUrl) && Objects
            .equals(phoneNumber, userInfo.phoneNumber) && Objects
            .equals(description, userInfo.description)
            && notificationType == userInfo.notificationType;
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(firstname, lastname, role, imageUrl, phoneNumber, description, notificationType);
    }
}
