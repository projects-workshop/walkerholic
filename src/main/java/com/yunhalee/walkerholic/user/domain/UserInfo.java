package com.yunhalee.walkerholic.user.domain;

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

    @Column(name = "firstname", nullable = false, length = 45)
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 45)
    private String lastname;
//
//    @Column(length = 128, nullable = false, unique = true)
//    private String email;
//
//    @Column(nullable = false)
//    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(length = 13)
    private String phoneNumber;

//    @Column(name = "level")
//    @Enumerated(EnumType.STRING)
//    private Level level = Level.Starter;
//
//    private Integer score = 0;

    private String description;

//    @Column(name = "provider_type")
//    @Enumerated(EnumType.STRING)
//    private ProviderType providerType;

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
//
//    public Integer getScore(){
//        return score;
//    }
//
//    public void updateLevel(UserActivity userActivity) {
//        addScore(userActivity);
//        changeLevel();
//    }
//
//    private void addScore(UserActivity userActivity) {
//        if (userActivity.finished()) {
//            score += userActivity.getScore();
//        }
//    }
//
//    private void changeLevel() {
//        this.level = Arrays.stream(Level.values())
//            .filter(level -> level.getMin() <= score && level.getMax() >= score)
//            .findFirst()
//            .orElse(this.level);
//    }
//
//    public void deleteUserActivity(UserActivity userActivity) {
//        minusScore(userActivity);
//        changeLevel();
//    }
//
//    private void minusScore(UserActivity userActivity){
//        if (userActivity.finished()) {
//            score -= userActivity.getScore();
//        }
//    }

    public boolean isSeller() {
        return this.role == Role.SELLER;
    }

    public String getRoleName() {
        return this.role.name();
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

    public void update(User toUser) {
        this.firstname = toUser.getFirstname();
        this.lastname = toUser.getLastname();
        this.role = toUser.getRole();
        this.imageUrl = toUser.getImageUrl();
        this.phoneNumber = toUser.getPhoneNumber();
        this.description = toUser.getDescription();
    }
}
