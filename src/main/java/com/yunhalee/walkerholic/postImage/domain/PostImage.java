package com.yunhalee.walkerholic.postImage.domain;

import com.yunhalee.walkerholic.post.domain.Post;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import lombok.NonNull;

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

    @Builder
    public PostImage(Integer id, @NonNull String name, @NonNull String filePath, Post post) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
        this.post = post;
    }

    public static PostImage of(String name, String filePath, Post post) {
        return PostImage.builder()
            .name(name)
            .filePath(filePath)
            .post(post).build();
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
