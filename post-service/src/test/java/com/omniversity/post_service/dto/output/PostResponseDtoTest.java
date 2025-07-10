package com.omniversity.post_service.dto.output;

import com.omniversity.post_service.entity.PostStatus;
import org.junit.jupiter.api.Test;
import java.time.Instant;
import static org.junit.jupiter.api.Assertions.*;

class PostResponseDtoTest {

    @Test
    void testPostResponseDto() {
        Instant now = Instant.now();
        PostResponseDto postResponseDto = new PostResponseDto();
        postResponseDto.setId(1L);
        postResponseDto.setCreatedAt(now);
        postResponseDto.setUpdatedAt(now);
        postResponseDto.setTitle("Response Title");
        postResponseDto.setContent("Response Content");
        postResponseDto.setAuthorId(500L);
        postResponseDto.setCommunityId(600L);
        postResponseDto.setStatus(PostStatus.PUBLISHED);
        postResponseDto.setEdited(true);

        assertEquals(1L, postResponseDto.getId());
        assertEquals(now, postResponseDto.getCreatedAt());
        assertEquals(now, postResponseDto.getUpdatedAt());
        assertEquals("Response Title", postResponseDto.getTitle());
        assertEquals("Response Content", postResponseDto.getContent());
        assertEquals(500L, postResponseDto.getAuthorId());
        assertEquals(600L, postResponseDto.getCommunityId());
        assertEquals(PostStatus.PUBLISHED, postResponseDto.getStatus());
        assertTrue(postResponseDto.isEdited());
    }
}
