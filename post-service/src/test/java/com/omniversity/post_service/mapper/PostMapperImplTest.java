package com.omniversity.post_service.mapper;

import com.omniversity.post_service.dto.input.PostCreateDto;
import com.omniversity.post_service.dto.input.PostUpdateDto;
import com.omniversity.post_service.dto.output.PostListItemDto;
import com.omniversity.post_service.dto.output.PostResponseDto;
import com.omniversity.post_service.entity.Post;
import com.omniversity.post_service.entity.PostStatus;
import com.omniversity.post_service.exception.custom.InvalidInputException;
import com.omniversity.post_service.mapper.impl.PostMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PostMapperImplTest {

    private PostMapperImpl postMapper;

    @BeforeEach
    void setUp() {
        postMapper = new PostMapperImpl();
    }

    @Test
    void toEntity_Success() {
        PostCreateDto dto = new PostCreateDto();
        dto.setTitle("Test Title");
        dto.setContent("Test Content");
        dto.setAuthorId(1L);
        dto.setCommunityId(2L);

        Post post = postMapper.toEntity(dto);

        assertNotNull(post);
        assertEquals("Test Title", post.getTitle());
        assertEquals("Test Content", post.getContent());
        assertEquals(1L, post.getAuthorId());
        assertEquals(2L, post.getCommunityId());
        assertEquals(PostStatus.PUBLISHED, post.getStatus());
        assertFalse(post.isDeleted());
    }

    @Test
    void toEntity_InvalidInputException_NullTitle() {
        PostCreateDto dto = new PostCreateDto();
        dto.setTitle(null);
        dto.setContent("Test Content");
        dto.setAuthorId(1L);
        dto.setCommunityId(2L);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> postMapper.toEntity(dto));
        assertEquals("Post title cannot be empty.", exception.getMessage());
    }

    @Test
    void toEntity_InvalidInputException_EmptyTitle() {
        PostCreateDto dto = new PostCreateDto();
        dto.setTitle("   ");
        dto.setContent("Test Content");
        dto.setAuthorId(1L);
        dto.setCommunityId(2L);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> postMapper.toEntity(dto));
        assertEquals("Post title cannot be empty.", exception.getMessage());
    }

    @Test
    void toEntity_InvalidInputException_NullContent() {
        PostCreateDto dto = new PostCreateDto();
        dto.setTitle("Test Title");
        dto.setContent(null);
        dto.setAuthorId(1L);
        dto.setCommunityId(2L);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> postMapper.toEntity(dto));
        assertEquals("Post content cannot be empty.", exception.getMessage());
    }

    @Test
    void toEntity_InvalidInputException_EmptyContent() {
        PostCreateDto dto = new PostCreateDto();
        dto.setTitle("Test Title");
        dto.setContent("   ");
        dto.setAuthorId(1L);
        dto.setCommunityId(2L);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> postMapper.toEntity(dto));
        assertEquals("Post content cannot be empty.", exception.getMessage());
    }

    @Test
    void toEntity_InvalidInputException_NullAuthorId() {
        PostCreateDto dto = new PostCreateDto();
        dto.setTitle("Test Title");
        dto.setContent("Test Content");
        dto.setAuthorId(null);
        dto.setCommunityId(2L);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> postMapper.toEntity(dto));
        assertEquals("Author ID cannot be null.", exception.getMessage());
    }

    @Test
    void toEntity_InvalidInputException_NullCommunityId() {
        PostCreateDto dto = new PostCreateDto();
        dto.setTitle("Test Title");
        dto.setContent("Test Content");
        dto.setAuthorId(1L);
        dto.setCommunityId(null);

        InvalidInputException exception = assertThrows(InvalidInputException.class, () -> postMapper.toEntity(dto));
        assertEquals("Community ID cannot be null.", exception.getMessage());
    }

    @Test
    void updateEntity_Success() {
        Post post = new Post();
        post.setTitle("Old Title");
        post.setContent("Old Content");
        post.setCommunityId(1L);
        post.setStatus(PostStatus.PUBLISHED);

        PostUpdateDto dto = new PostUpdateDto();
        dto.setTitle("New Title");
        dto.setContent("New Content");
        dto.setCommunityId(2L);
        dto.setStatus(PostStatus.ARCHIVED);

        postMapper.updateEntity(post, dto);

        assertEquals("New Title", post.getTitle());
        assertEquals("New Content", post.getContent());
        assertEquals(2L, post.getCommunityId());
        assertEquals(PostStatus.ARCHIVED, post.getStatus());
    }

    @Test
    void updateEntity_PartialUpdate() {
        Post post = new Post();
        post.setTitle("Old Title");
        post.setContent("Old Content");
        post.setCommunityId(1L);
        post.setStatus(PostStatus.PUBLISHED);

        PostUpdateDto dto = new PostUpdateDto();
        dto.setTitle("New Title");
        dto.setCommunityId(2L);

        postMapper.updateEntity(post, dto);

        assertEquals("New Title", post.getTitle());
        assertEquals("Old Content", post.getContent()); // Should remain unchanged
        assertEquals(2L, post.getCommunityId());
        assertEquals(PostStatus.PUBLISHED, post.getStatus()); // Should remain unchanged
    }

    @Test
    void updateEntity_NoUpdate() {
        Post post = new Post();
        post.setTitle("Old Title");
        post.setContent("Old Content");
        post.setCommunityId(1L);
        post.setStatus(PostStatus.PUBLISHED);

        PostUpdateDto dto = new PostUpdateDto(); // All fields null

        postMapper.updateEntity(post, dto);

        assertEquals("Old Title", post.getTitle());
        assertEquals("Old Content", post.getContent());
        assertEquals(1L, post.getCommunityId());
        assertEquals(PostStatus.PUBLISHED, post.getStatus());
    }

    @Test
    void toResponseDto_Success() {
        Post post = new Post();
        post.setId(1L);
        post.setCreatedAt(Instant.now());
        post.setUpdatedAt(Instant.now());
        post.setTitle("Test Title");
        post.setContent("Test Content");
        post.setAuthorId(1L);
        post.setCommunityId(2L);
        post.setStatus(PostStatus.PUBLISHED);

        PostResponseDto dto = postMapper.toResponseDto(post);

        assertNotNull(dto);
        assertEquals(post.getId(), dto.getId());
        assertEquals(post.getCreatedAt(), dto.getCreatedAt());
        assertEquals(post.getUpdatedAt(), dto.getUpdatedAt());
        assertEquals(post.getTitle(), dto.getTitle());
        assertEquals(post.getContent(), dto.getContent());
        assertEquals(post.getAuthorId(), dto.getAuthorId());
        assertEquals(post.getCommunityId(), dto.getCommunityId());
        assertEquals(post.getStatus(), dto.getStatus());
    }

    @Test
    void toListItemDto_Success() {
        Post post = new Post();
        post.setId(1L);
        post.setCreatedAt(Instant.now());
        post.setTitle("Test Title");
        post.setAuthorId(1L);
        post.setStatus(PostStatus.PUBLISHED);

        PostListItemDto dto = postMapper.toListItemDto(post);

        assertNotNull(dto);
        assertEquals(post.getId(), dto.getId());
        assertEquals(post.getTitle(), dto.getTitle());
        assertEquals(post.getCreatedAt(), dto.getCreatedAt());
        assertEquals(post.getAuthorId(), dto.getAuthorId());
        assertEquals(post.getStatus(), dto.getStatus());
    }
}