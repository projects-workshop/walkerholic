package com.yunhalee.walkerholic.post.domain;

import java.util.Objects;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "post_image")
@Getter
@NoArgsConstructor
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public PostImage(Integer id, String name, String filePath) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
    }

    private PostImage(String name, String filePath, Post post) {
        this.name = name;
        this.filePath = filePath;
        this.post = post;
    }

    public static PostImage of(String name, String filePath, Post post) {
        PostImage postImage = new PostImage(name, filePath, post);
        return postImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostImage postImage = (PostImage) o;
        return Objects.equals(id, postImage.id) && Objects
            .equals(name, postImage.name) && Objects.equals(filePath, postImage.filePath)
            && Objects.equals(post, postImage.post);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, filePath, post);
    }
}
