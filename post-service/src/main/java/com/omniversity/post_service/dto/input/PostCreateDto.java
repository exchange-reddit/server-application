package com.omniversity.post_service.dto.input;

import lombok.Data;

@Data
public class PostCreateDto {
    private Long id;
    private String title;
    private String content;
    private int authorId;
}