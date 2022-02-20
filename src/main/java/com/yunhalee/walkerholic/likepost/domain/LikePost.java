package com.yunhalee.walkerholic.likepost.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "like_post")
@Getter
@NoArgsConstructor
public class LikePost extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_post_id")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties({"posts"})
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public LikePost(Integer id, User user, Post post) {
        this.id = id;
        this.user = user;
        this.post = post;
    }

    private LikePost(User user, Post post) {
        this.user = user;
        this.post = post;
    }

    public static LikePost of(User user, Post post) {
        LikePost likePost = new LikePost(user, post);
        post.addLikePost(likePost);
        return likePost;
    }
}
