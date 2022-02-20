package com.yunhalee.walkerholic.post.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class PostResponses {

    private List<PostResponse> posts;
    private Long totalElement;
    private Integer totalPage;

    public PostResponses(List<PostResponse> posts, Long totalElement, Integer totalPage) {
        this.posts = posts;
        this.totalElement = totalElement;
        this.totalPage = totalPage;
    }

    public static PostResponses of(List<PostResponse> posts, Long totalElement, Integer totalPage) {
        return new PostResponses(posts, totalElement, totalPage);
    }
}
