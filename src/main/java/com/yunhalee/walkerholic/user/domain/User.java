package com.yunhalee.walkerholic.user.domain;

import com.yunhalee.walkerholic.follow.domain.Follow;
import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.user.dto.UserRequest;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.security.oauth.domain.ProviderType;
import java.util.Arrays;
import java.util.Objects;
import javassist.bytecode.MethodParametersAttribute;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "user")
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

//    @Column(name = "firstname", nullable = false, length = 45)
//    private String firstname;
//
//    @Column(name = "lastname", nullable = false, length = 45)
//    private String lastname;
//
//    @Column(length = 128, nullable = false, unique = true)
//    private String email;
//
//    @Column(nullable = false)
//    private String password;
//
//    @Column(name = "role")
//    @Enumerated(EnumType.STRING)
//    private Role role;
//
//    @Column(name = "image_url")
//    private String imageUrl;
//
//    @Column(length = 13)
//    private String phoneNumber;
//
//    @Column(name = "level")
//    @Enumerated(EnumType.STRING)
//    private Level level = Level.Starter;
//
//    private Integer score = 0;
//
//    private String description;
//
//    private boolean isSeller;
    @Embedded
    private UserInfo userInfo;

    @Embedded
    private UserAuth userAuth;

    @Embedded
    private UserLevel userLevel = new UserLevel();

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Post> posts = new HashSet<>();

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<LikePost> likePosts = new HashSet<>();

//    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Follow> followings = new HashSet<>();
//
//    @OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Follow> followers = new HashSet<>();


    @Builder
    public User(Integer id, @NonNull String firstname, @NonNull String lastname, @NonNull String email, @NonNull String password, String imageUrl, String phoneNumber, String description, @NonNull Role role) {
        this.id = id;
        this.userInfo = UserInfo.builder()
            .firstname(firstname)
            .lastname(lastname)
            .imageUrl(imageUrl)
            .phoneNumber(phoneNumber)
            .description(description)
            .role(role).build();
        this.userAuth = UserAuth.builder()
            .email(email)
            .password(password).build();
    }

//    public User(String firstname, String lastname, String email, String password, String phoneNumber, String description, Role role) {
//        this.userInfo = UserInfo.builder()
//            .firstname(firstname)
//            .lastname(lastname)
//            .phoneNumber(phoneNumber)
//            .description(description)
//            .role(role).build();
//        this.userAuth = UserAuth.builder()
//            .email(email)
//            .password(password).build();
//    }

    public void update(User toUser) {
        this.userAuth.update(toUser);
        this.userInfo.update(toUser);
    }

    public String getFullName() {
        return this.userInfo.getFullname();
    }

    public Integer getScore(){
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
