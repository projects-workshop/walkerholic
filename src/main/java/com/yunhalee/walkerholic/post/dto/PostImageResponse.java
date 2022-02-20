package com.yunhalee.walkerholic.post.dto;

import com.yunhalee.walkerholic.post.domain.PostImage;
import lombok.Getter;

@Getter
public class PostImageResponse {

    private Integer id;
    private String imageUrl;

    private PostImageResponse(Integer id, String imageUrl) {
        this.id = id;
        this.imageUrl = imageUrl;
    }

    public static PostImageResponse of(PostImage postImage) {
        return new PostImageResponse(postImage.getId(), postImage.getFilePath());
    }
}
