package com.omniversity.post_service.controller;

import com.omniversity.post_service.dto.input.PostCreateDto;
import com.omniversity.post_service.dto.input.PostUpdateDto;
import com.omniversity.post_service.dto.output.PostListItemDto;
import com.omniversity.post_service.dto.output.PostResponseDto;
import com.omniversity.post_service.service.PostService;
import com.omniversity.post_service.service.storage.StorageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;
    private final StorageService storageService;

    public PostController(PostService postService, StorageService storageService) {
        this.postService = postService;
        this.storageService = storageService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPost(
            @RequestPart("post") PostCreateDto postCreateDto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            PostResponseDto savedPostDto = postService.createPost(postCreateDto, file);
            return ResponseEntity.ok(savedPostDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create post: " + e.getMessage());
        }
    }

    @GetMapping("/all-posts")
    public ResponseEntity<?> getAllPosts() {
        try {
            List<PostResponseDto> posts = postService.getAllPosts();
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve posts: " + e.getMessage());
        }
    }

    @GetMapping("/list-items")
    public ResponseEntity<?> getAllPostListItems() {
        try {
            List<PostListItemDto> posts = postService.getAllPostListItems();
            return ResponseEntity.ok(posts);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to retrieve post summaries: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        try {
            PostResponseDto postDto = postService.getPostById(id);
            return ResponseEntity.ok(postDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Post not found: " + e.getMessage());
        }
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestPart("update") PostUpdateDto postUpdateDto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        try {
            PostResponseDto updatedPost = postService.updatePost(id, postUpdateDto, file);
            return ResponseEntity.ok(updatedPost);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update post: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        try {
            postService.deletePost(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete post: " + e.getMessage());
        }
    }
}
