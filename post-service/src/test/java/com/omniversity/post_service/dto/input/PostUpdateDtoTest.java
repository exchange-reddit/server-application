package com.omniversity.post_service.dto.input;

import com.omniversity.post_service.entity.PostStatus;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostUpdateDtoTest {

    @Test
    void testPostUpdateDto() {
        PostUpdateDto postUpdateDto = new PostUpdateDto();
        postUpdateDto.setTitle("Updated Title");
        postUpdateDto.setContent("Updated Content");
        postUpdateDto.setCommunityId(300L);
        postUpdateDto.setStatus(PostStatus.PUBLISHED);

        assertEquals("Updated Title", postUpdateDto.getTitle());
        assertEquals("Updated Content", postUpdateDto.getContent());
        assertEquals(300L, postUpdateDto.getCommunityId());
        assertEquals(PostStatus.PUBLISHED, postUpdateDto.getStatus());
    }
}
