package com.yunhalee.walkerholic.post.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostCreateDTO {

    private Integer id;

    private String title;

    private String content;

    private Integer userId;

    public PostCreateDTO(Integer id, String title, String content, Integer userId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public PostCreateDTO(String title, String content, Integer userId) {
        this.title = title;
        this.content = content;
        this.userId = userId;
    }
}
