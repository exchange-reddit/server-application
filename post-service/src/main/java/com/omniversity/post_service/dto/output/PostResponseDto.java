package com.omniversity.post_service.dto.output;

import lombok.Data;
import java.time.Instant;
import com.omniversity.post_service.entity.PostStatus;

@Data
public class PostResponseDto {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private String title;
    private String content;
    private Long authorId;
    private Long communityId;
    private PostStatus status;
    private boolean isEdited;
}
