package com.yunhalee.walkerholic.postImage.dto;

import com.yunhalee.walkerholic.postImage.domain.PostImage;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
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
