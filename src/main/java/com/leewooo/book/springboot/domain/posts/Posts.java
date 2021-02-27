package com.leewooo.book.springboot.domain.posts;

import com.leewooo.book.springboot.domain.BaseTimeEntity;
import com.leewooo.book.springboot.web.dto.PostsUpdateRequestDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //length column의 크기
    @Column(length = 500,nullable = false)
    private String title;

    //columnDefinition 입력되지 않았을 경우 default값
    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @Column
    private String author;

    //Builder (생성자와 동일한 역할이지만 더 직관적이게 값을 입력 가능
    @Builder
    public Posts(String title, String content, String author){
        this.title = title;
        this.content= content;
        this.author = author;
    }

    public void update(PostsUpdateRequestDTO postsUpdateRequestDTO){
        this.title = postsUpdateRequestDTO.getTitle();
        this.content = postsUpdateRequestDTO.getContent();
    }
}
