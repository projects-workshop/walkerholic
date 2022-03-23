package com.yunhalee.walkerholic.postImage.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostImageResponses {

    private List<PostImageResponse> postImages;

    private PostImageResponses(List<PostImageResponse> postImages) {
        this.postImages = postImages;
    }

    public static PostImageResponses of(List<PostImageResponse> postImages) {
        return new PostImageResponses(postImages);
    }
}
