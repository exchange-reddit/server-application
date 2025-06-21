package com.omniversity.post_service.controller;

import com.omniversity.post_service.dto.input.PostCreateDto;
import com.omniversity.post_service.dto.input.PostUpdateDto;
import com.omniversity.post_service.dto.output.PostListItemDto;
import com.omniversity.post_service.dto.output.PostResponseDto;
import com.omniversity.post_service.entity.Post;
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

    /**
     * Creates post with the given parameters. Note that swagger API testing will not work,
     * because the postCreateDto must be of content-type: application/json, but swagger
     * defaults to application/octet-stream. Need to use curl command instead.
     *
     * TODO: will probably need to separate the APIs between postCreateDto and media files.
     *
     * @param postCreateDto must be Content-type: application/json
     * @param file is multimedia file.
     * @return ResponseEntity.
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDto> createPost(
            @RequestPart("post") PostCreateDto postCreateDto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        PostResponseDto savedPostDto = postService.createPost(postCreateDto, file);
        return ResponseEntity.ok(savedPostDto);
    }

    /**
     * Returns list of all posts with full information. This is a longer version of /all-posts-shorter.
     *
     * @return list of posts with their full information.
     */
    @GetMapping("/all-posts")
    public ResponseEntity<?> getAllPosts() {
        List<PostResponseDto> posts = postService.getAllPosts();
        return ResponseEntity.ok(posts);
    }

    /**
     * Returns list of all posts with only a subset of attributes. This is a shorter version of
     * /all-posts.
     *
     * TODO: Need to implement pagination for this API.
     *
     * @return list of all post items with only a subset of attributes.
     */
    @GetMapping("/all-posts-shorter")
    public ResponseEntity<?> getAllPostListItems() {
        List<PostListItemDto> posts = postService.getAllPostListItems();
        return ResponseEntity.ok(posts);
    }

    /**
     * Gets a post by its id.
     *
     * @param id Post ID.
     * @return postDto.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPostById(@PathVariable Long id) {
        PostResponseDto postDto = postService.getPostById(id);
        return ResponseEntity.ok(postDto);
    }

    /**
     * Updates the post with the given id.
     *
     * @param id
     * @param postUpdateDto
     * @param file
     * @return
     */
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updatePost(
            @PathVariable Long id,
            @RequestPart("update") PostUpdateDto postUpdateDto,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) {
        PostResponseDto updatedPost = postService.updatePost(id, postUpdateDto, file);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
