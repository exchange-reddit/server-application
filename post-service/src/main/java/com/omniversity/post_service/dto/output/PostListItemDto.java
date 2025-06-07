package com.omniversity.post_service.dto.output;

import lombok.Data;
import java.time.Instant;
import com.omniversity.post_service.entity.PostStatus;

@Data
public class PostListItemDto {
    private Long id;
    private String title;
    private Instant createdAt;
    private Long authorId;
    private PostStatus status;
}
