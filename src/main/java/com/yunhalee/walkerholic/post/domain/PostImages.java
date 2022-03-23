package com.yunhalee.walkerholic.post.domain;

import com.yunhalee.walkerholic.postImage.domain.PostImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class PostImages {

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> postImages = new ArrayList<>();

    public PostImages() {
    }

    private PostImages(List<PostImage> postImages) {
        this.postImages = postImages;
    }

    public static PostImages of(PostImage... postImages) {
        return new PostImages(new ArrayList<>(Arrays.asList(postImages)));
    }

    public void addPostImage(PostImage postImage) {
        postImages.add(postImage);
    }


    public List<PostImage> getPostImages() {
        return Collections.unmodifiableList(postImages);
    }

    public void deletePostImages(List<String> deletedImages) {
        this.postImages = this.postImages.stream()
            .filter(postImage -> !deletedImages.contains(postImage.getFilePath()))
            .collect(Collectors.toList());
    }
}
