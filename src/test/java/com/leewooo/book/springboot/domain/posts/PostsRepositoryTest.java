package com.leewooo.book.springboot.domain.posts;


import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    private PostsRepository postsRepository;

    @Test
    public void 게시글저장_불러오기(){

        //given
        String title = "test";
        String content = "content";

        postsRepository.save(Posts.builder().title(title).content(content).author("이우길").build());

        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        assertThat(postsList.get(0).getTitle()).isEqualTo(title);
        assertThat(postsList.get(0).getContent()).isEqualTo(content);
        assertThat(postsList.size()).isEqualTo(1);
    }

}