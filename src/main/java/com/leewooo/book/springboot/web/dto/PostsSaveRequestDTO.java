package com.leewooo.book.springboot.web.dto;

import com.leewooo.book.springboot.domain.posts.Posts;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostsSaveRequestDTO {

    private String title;
    private String content;
    private String author;

    @Builder
    public PostsSaveRequestDTO(String title,String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    //요청이 들어온 값으로 Posts를 생성해서 반환
    public Posts toEntity(){
        return Posts.builder().title(title).content(content).author(author).build();
    }
}
