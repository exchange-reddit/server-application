package com.omniversity.post_service.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostDtoTest {

    @Test
    void testPostDto() {
        PostDto postDto = new PostDto();
        postDto.setId(1L);
        postDto.setTitle("Test Title");
        postDto.setContent("Test Content");
        postDto.setAuthor("Test Author");

        assertEquals(1L, postDto.getId());
        assertEquals("Test Title", postDto.getTitle());
        assertEquals("Test Content", postDto.getContent());
        assertEquals("Test Author", postDto.getAuthor());
    }
}
