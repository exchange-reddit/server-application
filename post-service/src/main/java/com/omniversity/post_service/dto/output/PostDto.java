package com.omniversity.post_service.dto.output;


import java.time.Instant;

import lombok.Data;

@Data
public class PostDto {
    private Long id;
    private Instant createdAt;
    private String title;
    private String content;
    private int authorId;
}