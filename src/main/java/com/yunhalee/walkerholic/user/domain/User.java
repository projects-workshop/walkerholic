package com.yunhalee.walkerholic.user.domain;

import com.yunhalee.walkerholic.follow.domain.Follow;
import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.security.oauth.domain.ProviderType;
import java.util.Arrays;
import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "firstname", nullable = false, length = 45)
    private String firstname;

    @Column(name = "lastname", nullable = false, length = 45)
    private String lastname;

    @Column(length = 128, nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(length = 13)
    private String phoneNumber;

    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    private Level level = Level.Starter;

    private Integer score = 0;

    private String description;

    private boolean isSeller;

    @Column(name = "provider_type")
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LikePost> likePosts = new HashSet<>();

    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> followings = new HashSet<>();

    @OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> followers = new HashSet<>();

    public User(Integer id, String firstname, String lastname, String email, String password, Role role) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String firstname, String lastname, String email, String password, Role role) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Transient
    public String getFullname() {
        return this.firstname + this.lastname;
    }

    public Integer getScore(){
        return score;
    }

    //    비지니스 로직
    public void updateLevel(UserActivity userActivity) {
        addScore(userActivity);
        changeLevel();
    }

    private void addScore(UserActivity userActivity) {
        if (userActivity.finished()) {
            score += userActivity.getScore();
        }
    }

    private void changeLevel() {
        this.level = Arrays.stream(Level.values())
            .filter(level -> level.getMin() <= score && level.getMax() >= score)
            .findFirst()
            .orElse(this.level);
    }

    public void deleteUserActivity(UserActivity userActivity) {
        minusScore(userActivity);
        changeLevel();
    }

    private void minusScore(UserActivity userActivity){
        score -= userActivity.getScore();
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
        return isSeller == user.isSeller && Objects.equals(id, user.id) && Objects
            .equals(firstname, user.firstname) && Objects.equals(lastname, user.lastname)
            && Objects.equals(email, user.email) && Objects
            .equals(password, user.password) && role == user.role && Objects
            .equals(imageUrl, user.imageUrl) && Objects.equals(phoneNumber, user.phoneNumber)
            && level == user.level && Objects.equals(score, user.score) && Objects
            .equals(description, user.description) && providerType == user.providerType;
    }

    @Override
    public int hashCode() {
        return Objects
            .hash(id, firstname, lastname, email, password, role, imageUrl, phoneNumber, level,
                score,
                description, isSeller, providerType);
    }
}
