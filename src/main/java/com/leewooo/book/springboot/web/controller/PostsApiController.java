package com.leewooo.book.springboot.web.controller;

import com.leewooo.book.springboot.service.posts.PostsService;
import com.leewooo.book.springboot.web.dto.PostsResponseDTO;
import com.leewooo.book.springboot.web.dto.PostsSaveRequestDTO;
import com.leewooo.book.springboot.web.dto.PostsUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDTO postsSaveRequestDTO){
        return postsService.save(postsSaveRequestDTO);
    }//save

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDTO postsUpdateRequestDTO){
        return postsService.update(id,postsUpdateRequestDTO);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDTO findById(@PathVariable Long id){
        return postsService.findById(id);
    }


    @DeleteMapping("/api/v1/posts/{id}")
    public Long delete(@PathVariable Long id){
        postsService.delete(id);
        return id;
    }
}
