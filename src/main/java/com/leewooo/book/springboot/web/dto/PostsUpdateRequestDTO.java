package com.leewooo.book.springboot.web.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PostsUpdateRequestDTO {

    private String title;
    private String content;

    @Builder
    public PostsUpdateRequestDTO(String title, String content){
        this.title = title;
        this.content = content;
    }
}
