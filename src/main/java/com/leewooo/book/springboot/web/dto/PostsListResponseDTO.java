package com.leewooo.book.springboot.web.dto;

import com.leewooo.book.springboot.domain.posts.Posts;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostsListResponseDTO {

    private Long id;
    private String title;
    private String author;
    private LocalDateTime modifiedDate;

    public PostsListResponseDTO(Posts entity){
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.author =entity.getAuthor();
        this.modifiedDate = entity.getModifiedDate();
    }
}
