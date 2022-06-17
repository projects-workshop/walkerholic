package com.yunhalee.walkerholic.post.domain;

import com.yunhalee.walkerholic.common.domain.BaseTimeEntity;
import com.yunhalee.walkerholic.likepost.domain.LikePost;
import com.yunhalee.walkerholic.postImage.domain.PostImage;
import com.yunhalee.walkerholic.user.domain.User;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Set;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "post", indexes = @Index(name = "idx_title", columnList = "title"))
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Embedded
    private PostImages postImages;

    @Embedded
    private LikePosts likePosts;

    public Post(Integer id, String title, String content) {
        this.id = id;
        this.postImages = new PostImages();
        this.likePosts = new LikePosts();
        this.title = title;
        this.content = content;
    }

    public Post(String title, String content, PostImages postImages, User user, LikePosts likePosts) {
        this.title = title;
        this.content = content;
        this.postImages = postImages;
        this.user = user;
        this.likePosts = likePosts;
    }

    public Post(Integer id, String title, String content, User user) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.postImages = new PostImages();
        this.likePosts = new LikePosts();
        this.user = user;
    }

    public static Post of(String title, String content, User user) {
        return new Post(title, content, new PostImages(), user, new LikePosts());
    }

    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void addLikePost(LikePost likePost) {
        this.likePosts.addLikePost(likePost);
    }

    public void addPostImage(PostImage postImage) {
        this.postImages.addPostImage(postImage);
    }

    public void deletePostImage(List<String> deletedImages) {
        this.postImages.deletePostImages(deletedImages);
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public List<PostImage> getPostImages() {
        return postImages.getPostImages();
    }

    public User getUser() {
        return user;
    }

    public Set<LikePost> getLikePosts() {
        return likePosts.getLikePosts();
    }

}
