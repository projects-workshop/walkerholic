package com.yunhalee.walkerholic.post.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class SimplePostResponses {

    private List<SimplePostResponse> posts;
    private Long totalElement;
    private Integer totalPage;

    public SimplePostResponses(List<SimplePostResponse> posts, Long totalElement, Integer totalPage) {
        this.posts = posts;
        this.totalElement = totalElement;
        this.totalPage = totalPage;
    }

    public static SimplePostResponses of(List<SimplePostResponse> posts, Long totalElement, Integer totalPage){
        return new SimplePostResponses(posts, totalElement, totalPage);
    }

}
