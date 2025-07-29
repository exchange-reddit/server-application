package com.omniversity.post_service.dto.input;

import com.omniversity.post_service.entity.PostStatus;
import lombok.Data;

@Data
public class PostUpdateDto {
    private String title;
    private String content;
    private Long sectionId;
    private PostStatus status;
}
