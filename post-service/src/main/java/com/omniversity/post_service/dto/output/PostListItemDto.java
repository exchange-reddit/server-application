package com.omniversity.post_service.dto.output;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.Instant;
import com.omniversity.post_service.entity.PostStatus;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class PostListItemDto {
    private Long id;
    private String title;
    private Instant createdAt;
    private Long authorId;
    private PostStatus status;
}
