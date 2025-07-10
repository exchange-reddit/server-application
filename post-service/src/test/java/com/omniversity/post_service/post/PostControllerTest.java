package com.omniversity.post_service.post;

import com.omniversity.post_service.controller.PostController;
import com.omniversity.post_service.dto.output.PostListItemDto;
import com.omniversity.post_service.dto.input.PostUpdateDto;
import com.omniversity.post_service.dto.output.PostResponseDto;
import com.omniversity.post_service.entity.Post;
import org.springframework.web.multipart.MultipartFile;
import com.omniversity.post_service.entity.PostStatus;
import com.omniversity.post_service.mapper.PostMapper;
import com.omniversity.post_service.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {
    @Mock
    private PostService postService;

    @Mock
    private PostMapper postMapper;

    @InjectMocks
    private PostController postController;

    private Post post;

    @BeforeEach
    void setUp() {
        post = new Post();
        post.setId(1L);
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());
        post.setTitle("testTitle");
        post.setContent("testContent");
        post.setAuthorId(1L);
        post.setCommunityId(1L);
        post.setDeleted(false);
        post.setStatus(PostStatus.PUBLISHED);
        post.setAttachmentPath("testPath");
    }

    @Test
    void testGetAllPosts_Success() {
        PostResponseDto expectedDto = new PostResponseDto();
        List<PostResponseDto> expectedList = List.of(expectedDto);

        when(postService.getAllPosts()).thenReturn(expectedList);

        ResponseEntity<List<PostResponseDto>> response = (ResponseEntity<List<PostResponseDto>>) postController.getAllPosts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(List.of(expectedDto), response.getBody());
    }

    @Test
    void testGetAllPostListItems_Success() {
        PostListItemDto expectedDto = new PostListItemDto();
        List<PostListItemDto> expectedList = List.of(expectedDto);

        when(postService.getAllPostListItems()).thenReturn(expectedList);

        ResponseEntity<List<PostListItemDto>> response = (ResponseEntity<List<PostListItemDto>>) postController.getAllPostListItems();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedList, response.getBody());
    }

    @Test
    void testGetPostById_Success() {
        Long postId = 1L;
        PostResponseDto expectedDto = new PostResponseDto();

        when(postService.getPostById(postId)).thenReturn(expectedDto);

        ResponseEntity<PostResponseDto> response = (ResponseEntity<PostResponseDto>) postController.getPostById(postId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDto, response.getBody());
    }

    @Test
    void testUpdatePost_Success() {
        Long postId = 1L;
        PostUpdateDto postUpdateDto = new PostUpdateDto();
        MultipartFile file = null;
        PostResponseDto expectedDto = new PostResponseDto();

        when(postService.updatePost(postId, postUpdateDto, file)).thenReturn(expectedDto);

        ResponseEntity<PostResponseDto> response = (ResponseEntity<PostResponseDto>) postController.updatePost(postId, postUpdateDto, file);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedDto, response.getBody());
    }

    @Test
    void testDeletePost_Success() {
        Long postId = 1L;

        ResponseEntity<?> response = postController.deletePost(postId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
