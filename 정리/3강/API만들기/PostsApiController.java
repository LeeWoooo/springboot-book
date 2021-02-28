package com.test.contoller.web;

import com.test.Service.posts.PostsService;
import com.test.contoller.web.dto.PostsResponseDTO;
import com.test.contoller.web.dto.PostsSaveRequestDTO;
import com.test.contoller.web.dto.PostsUpdateRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDTO postsSaveRequestDTO){
        return postsService.save(postsSaveRequestDTO);
    }

    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDTO requestDTO){
        return postsService.update(id,requestDTO);
    }

    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDTO findById(@PathVariable Long id){
        return postsService.findById(id);
    }

}
