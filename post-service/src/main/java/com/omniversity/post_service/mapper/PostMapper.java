package com.omniversity.post_service.mapper;

import com.omniversity.post_service.dto.input.PostCreateDto;
import com.omniversity.post_service.dto.input.PostUpdateDto;
import com.omniversity.post_service.dto.output.PostResponseDto;
import com.omniversity.post_service.dto.output.PostListItemDto;
import com.omniversity.post_service.entity.Post;

public interface PostMapper {

    Post toEntity(PostCreateDto dto);

    void updateEntity(Post post, PostUpdateDto dto);

    PostResponseDto toResponseDto(Post post);

    PostListItemDto toListItemDto(Post post);
}
