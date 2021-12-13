package com.yunhalee.walkerholic.likepost.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.post.domain.Post;
import com.yunhalee.walkerholic.user.domain.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "like_post")
@Getter
@Setter
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

    //    비지니스 로직
    public static LikePost likePost(Post post, User user) {
        LikePost likePost = new LikePost();
        likePost.setPost(post);
        likePost.setUser(user);
        return likePost;
    }
}
