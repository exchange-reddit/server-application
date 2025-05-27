package com.omniversity.post_service.controller;

import com.omniversity.post_service.dto.input.PostCreateDto;
import com.omniversity.post_service.dto.output.PostDto;
import com.omniversity.post_service.mapper.PostCreateMapper;
import com.omniversity.post_service.mapper.PostMapper;
import com.omniversity.post_service.service.PostService;
import com.omniversity.post_service.entity.Post;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final PostMapper postMapper;
    private final PostCreateMapper postCreateMapper;

    public PostController(PostService postService, PostMapper postMapper, PostCreateMapper postCreateMapper) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.postCreateMapper = postCreateMapper;
    }

    @PostMapping
    public ResponseEntity<PostCreateDto> createPost(@RequestBody PostCreateDto postCreateDto) {
        Post post = postMapper.toEntity(postCreateDto);
        Post savedPost = postService.createPost(post);
        return ResponseEntity.ok(postMapper.toDto(savedPost));
    }

    @GetMapping
    public ResponseEntity<List<PostDto>> getAllPosts() {
        List<Post> posts = postService.getAllPosts();
        return ResponseEntity.ok(postMapper.toDtoList(posts));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long id) {
        return postService.getPostById(id)
                .map(post -> ResponseEntity.ok(postMapper.toDto(post)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long id, @RequestBody PostDto postDto) {
        Post updatedPost = postMapper.toEntity(postDto);
        Post savedPost = postService.updatePost(id, updatedPost);
        return ResponseEntity.ok(postMapper.toDto(savedPost));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
