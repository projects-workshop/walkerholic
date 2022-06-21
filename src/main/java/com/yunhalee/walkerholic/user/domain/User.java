package com.yunhalee.walkerholic.user.domain;

import com.yunhalee.walkerholic.security.oauth.domain.OAuth2UserInfo;
import com.yunhalee.walkerholic.security.oauth.domain.ProviderType;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private UserInfo userInfo;

    @Embedded
    private UserAuth userAuth;

    @Embedded
    private UserLevel userLevel = new UserLevel();

    @Builder
    public User(Integer id, @NonNull String firstname, @NonNull String lastname, @NonNull String email, @NonNull String password, String imageUrl, String phoneNumber, String description, @NonNull Role role, ProviderType providerType) {
        this.id = id;
        this.userInfo = UserInfo.builder()
            .firstname(firstname)
            .lastname(lastname)
            .imageUrl(imageUrl)
            .phoneNumber(phoneNumber)
            .description(description)
            .notificationType(NotificationType.NONE)
            .role(role).build();
        this.userAuth = UserAuth.builder()
            .email(email)
            .password(password)
            .providerType(providerType).build();
    }

    public void update(User toUser) {
        this.userAuth.update(toUser);
        this.userInfo.update(toUser);
    }

    public void updateOAuth2User(OAuth2UserInfo userInfo) {
        this.userInfo.updateOAuth2User(userInfo);
    }

    public void changePassword(String changedPassword) {
        this.userAuth.changePassword(changedPassword);
    }

    public String getFullName() {
        return this.userInfo.getFullname();
    }

    public Integer getScore() {
        return this.userLevel.getScore();
    }

    public void updateLevel(UserActivity userActivity) {
        this.userLevel.updateLevel(userActivity);
    }

    public void deleteUserActivity(UserActivity userActivity) {
        this.userLevel.deleteUserActivity(userActivity);
    }

    public boolean isSeller() {
        return this.userInfo.isSeller();
    }

    public String getFirstname() {
        return userInfo.getFirstname();
    }

    public String getLastname() {
        return userInfo.getLastname();
    }

    public String getEmail() {
        return userAuth.getEmail();
    }

    public String getPassword() {
        return userAuth.getPassword();
    }

    public Role getRole() {
        return userInfo.getRole();
    }

    public String getRoleName() {
        return userInfo.getRoleName();
    }

    public String getImageUrl() {
        return userInfo.getImageUrl();
    }

    public String getPhoneNumber() {
        return userInfo.getPhoneNumber();
    }

    public Level getLevel() {
        return userLevel.getLevel();
    }

    public String getLevelName() {
        return userLevel.getLevelName();
    }

    public String getDescription() {
        return userInfo.getDescription();
    }

    public NotificationType getNotificationType() {
        return userInfo.getNotificationType();
    }

    public boolean isDefaultImage() {
        return userInfo.isDefaultImageUrl();
    }

    public ProviderType getProviderType() {
        return userAuth.getProviderType();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects
            .equals(userInfo, user.userInfo) && Objects.equals(userAuth, user.userAuth)
            && Objects.equals(userLevel, user.userLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userInfo, userAuth, userLevel);
    }
}
