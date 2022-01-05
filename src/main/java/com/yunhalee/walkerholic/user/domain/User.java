package com.yunhalee.walkerholic.user.domain;

import com.yunhalee.walkerholic.useractivity.domain.ActivityStatus;
import com.yunhalee.walkerholic.follow.domain.Follow;
import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.useractivity.domain.UserActivity;
import com.yunhalee.walkerholic.order.domain.Order;
import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.product.domain.Product;
import com.yunhalee.walkerholic.review.domain.Review;
import com.yunhalee.walkerholic.security.oauth.domain.ProviderType;
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
    private Level level;

    private String description;

    private boolean isSeller;

    @Column(name = "provider_type")
    @Enumerated(EnumType.STRING)
    private ProviderType providerType;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserActivity> activities = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Post> posts = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Order> orders = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Product> products = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Review> reviews = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<LikePost> likePosts = new HashSet<>();

    @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> followings = new HashSet<>();

    @OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> followers = new HashSet<>();


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

    @Transient
    public Integer getScore() {
        Integer score = 0;
        for (UserActivity activity : this.activities) {
            if (activity.getStatus() == ActivityStatus.FINISHED) {
                score += activity.getActivity().getScore();
            }
        }
        return score;
    }

    //    비지니스 로직
    public void addUserActivity(UserActivity userActivity) {
        Integer score = this.getScore();
        score += userActivity.getActivity().getScore();
        for (Level level : Level.values()) {
            if (level.getMin() <= score && level.getMax() >= score) {
                this.level = level;
                return;
            }
        }
    }

    public void deleteUserActivity() {
        Integer score = this.getScore();
        for (Level level : Level.values()) {
            if (level.getMin() <= score && level.getMax() >= score) {
                this.level = level;
                return;
            }
        }
    }
}
