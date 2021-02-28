package com.test.Service.posts;

import com.test.contoller.web.dto.PostsResponseDTO;
import com.test.contoller.web.dto.PostsSaveRequestDTO;
import com.test.contoller.web.dto.PostsUpdateRequestDTO;
import com.test.domain.post.Posts;
import com.test.domain.post.PostsRepositroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepositroy postsRepositroy;

    @Transactional
    public Long save(PostsSaveRequestDTO postsSaveRequestDTO){
        return postsRepositroy.save(postsSaveRequestDTO.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDTO requestDTO){
        Posts posts = postsRepositroy.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id)
        );
        posts.update(requestDTO.getTitle(), requestDTO.getContent());
        return id;
    }

    public PostsResponseDTO findById(Long id){
        Posts entity = postsRepositroy.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id)
        );
        return new PostsResponseDTO(entity);
    }

}
