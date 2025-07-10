package com.omniversity.post_service.dto.output;

import com.omniversity.post_service.entity.PostStatus;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class PostListItemDtoTest {

    @Test
    void testPostListItemDto() {
        Instant now = Instant.now();
        PostListItemDto postListItemDto = new PostListItemDto();
        postListItemDto.setId(1L);
        postListItemDto.setTitle("List Item Title");
        postListItemDto.setCreatedAt(now);
        postListItemDto.setAuthorId(400L);
        postListItemDto.setStatus(PostStatus.ARCHIVED);

        assertEquals(1L, postListItemDto.getId());
        assertEquals("List Item Title", postListItemDto.getTitle());
        assertEquals(now, postListItemDto.getCreatedAt());
        assertEquals(400L, postListItemDto.getAuthorId());
        assertEquals(PostStatus.ARCHIVED, postListItemDto.getStatus());
    }

    @Test
    void testAllArgsConstructor() {
        Instant now = Instant.now();
        PostListItemDto postListItemDto = new PostListItemDto(2L, "Another Title", now, 500L, PostStatus.PUBLISHED);

        assertEquals(2L, postListItemDto.getId());
        assertEquals("Another Title", postListItemDto.getTitle());
        assertEquals(now, postListItemDto.getCreatedAt());
        assertEquals(500L, postListItemDto.getAuthorId());
        assertEquals(PostStatus.PUBLISHED, postListItemDto.getStatus());
    }
}
