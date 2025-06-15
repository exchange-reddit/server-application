package com.omniversity.post_service.dto.input;

import lombok.Data;

@Data
public class PostCreateDto {
    private String title;
    private String content;
    private Long authorId;
    private Long communityId;
}
