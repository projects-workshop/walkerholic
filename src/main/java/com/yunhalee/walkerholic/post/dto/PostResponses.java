package com.yunhalee.walkerholic.post.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class PostResponses {

    private List<SimplePostResponse> posts;
    private Long totalElement;
    private Integer totalPage;

    public PostResponses(List<SimplePostResponse> posts, Long totalElement, Integer totalPage) {
        this.posts = posts;
        this.totalElement = totalElement;
        this.totalPage = totalPage;
    }

    public static PostResponses of(List<SimplePostResponse> posts, Long totalElement, Integer totalPage){
        return new PostResponses(posts, totalElement, totalPage);
    }

}
