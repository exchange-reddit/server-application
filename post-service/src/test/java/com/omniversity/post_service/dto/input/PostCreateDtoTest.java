package com.omniversity.post_service.dto.input;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostCreateDtoTest {

    @Test
    void testPostCreateDto() {
        PostCreateDto postCreateDto = new PostCreateDto();
        postCreateDto.setTitle("New Post Title");
        postCreateDto.setContent("New Post Content");
        postCreateDto.setAuthorId(100L);
        postCreateDto.setCommunityId(200L);

        assertEquals("New Post Title", postCreateDto.getTitle());
        assertEquals("New Post Content", postCreateDto.getContent());
        assertEquals(100L, postCreateDto.getAuthorId());
        assertEquals(200L, postCreateDto.getCommunityId());
    }
}
