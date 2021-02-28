package com.leewooo.book.springboot.service.posts;

import com.leewooo.book.springboot.domain.posts.Posts;
import com.leewooo.book.springboot.domain.posts.PostsRepository;
import com.leewooo.book.springboot.web.dto.PostsListResponseDTO;
import com.leewooo.book.springboot.web.dto.PostsResponseDTO;
import com.leewooo.book.springboot.web.dto.PostsSaveRequestDTO;
import com.leewooo.book.springboot.web.dto.PostsUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDTO postsSaveRequestDTO){
        return postsRepository.save(postsSaveRequestDTO.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDTO postsUpdateRequestDTO){
        Posts posts = postsRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디의 게시글이 존재하지 않습니다 id="+id)
        );
        posts.update(postsUpdateRequestDTO);
        return id;
    }

    @Transactional
    public void delete(Long id){
        postsRepository.deleteById(id);
    }

    public PostsResponseDTO findById(Long id){
        Posts posts = postsRepository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 아이디의 게시글이 존재하지 않습니다 id="+id)
        );
        return new PostsResponseDTO(posts);
    }

    public List<PostsListResponseDTO> findAll(){
        List<Posts> postsList = postsRepository.findAllDesc();
        List<PostsListResponseDTO> postsListResponseDTOList = new ArrayList<>();
        PostsListResponseDTO postsListResponseDTO = null;
        for(Posts entity : postsList){
            postsListResponseDTO = new PostsListResponseDTO(entity);
            postsListResponseDTOList.add(postsListResponseDTO);
        }
        return postsListResponseDTOList;
    }

}
