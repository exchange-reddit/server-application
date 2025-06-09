package com.omniversity.post_service.controller;

import com.omniversity.post_service.dto.input.PostCreateDto;
import com.omniversity.post_service.dto.input.PostUpdateDto;
import com.omniversity.post_service.dto.output.PostListItemDto;
import com.omniversity.post_service.dto.output.PostResponseDto;
import com.omniversity.post_service.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostCreateDto postCreateDto) {
        PostResponseDto savedPostDto = postService.createPost(postCreateDto);
        return ResponseEntity.ok(savedPostDto);
    }

    @GetMapping("/all-posts")
    public ResponseEntity<List<PostResponseDto>> getAllPosts() {
        List<PostResponseDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    // Optional: expose your ListItem DTO API
    @GetMapping("/list-items")
    public ResponseEntity<List<PostListItemDto>> getAllPostListItems() {
        List<PostListItemDto> posts = postService.getAllPostListItems();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable Long id) {
        PostResponseDto postDto = postService.getPostById(id);
        return ResponseEntity.ok(postDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long id, @RequestBody PostUpdateDto postUpdateDto) {
        PostResponseDto updatedPost = postService.updatePost(id, postUpdateDto);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
