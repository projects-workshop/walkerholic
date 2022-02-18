package com.yunhalee.walkerholic.post.domain;

import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
public class Post extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Integer id;

    @Column(nullable = false)
    private String title;

    @Column(length = 3000)
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<PostImage> postImages = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post")
    private Set<LikePost> likePosts = new HashSet<>();

    public Post(Integer id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }
}
