package com.omniversity.post_service.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PostEventTest {

    @Test
    void testPostEvent() {
        PostEvent postEvent = new PostEvent();
        assertNotNull(postEvent);
    }
}
