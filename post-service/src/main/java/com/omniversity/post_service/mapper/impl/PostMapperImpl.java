package com.omniversity.post_service.mapper.impl;

import com.omniversity.post_service.mapper.PostMapper;
import com.omniversity.post_service.dto.input.PostCreateDto;
import com.omniversity.post_service.dto.input.PostUpdateDto;
import com.omniversity.post_service.dto.output.PostResponseDto;
import com.omniversity.post_service.dto.output.PostListItemDto;
import com.omniversity.post_service.entity.Post;
import com.omniversity.post_service.entity.PostStatus;
import org.springframework.stereotype.Component;

@Component
public class PostMapperImpl implements PostMapper {

    @Override
    public Post toEntity(PostCreateDto dto) {
        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setAuthorId(dto.getAuthorId());
        post.setCommunityId(dto.getCommunityId());
        post.setStatus(PostStatus.PUBLISHED); // default status
        post.setDeleted(false); // default value
        return post;
    }

    @Override
    public void updateEntity(Post post, PostUpdateDto dto) {
        if (dto.getTitle() != null) {
            post.setTitle(dto.getTitle());
        }
        if (dto.getContent() != null) {
            post.setContent(dto.getContent());
        }
        if (dto.getCommunityId() != null) {
            post.setCommunityId(dto.getCommunityId());
        }
        if (dto.getStatus() != null) {
            post.setStatus(dto.getStatus());
        }
    }

    @Override
    public PostResponseDto toResponseDto(Post post) {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setUpdatedAt(post.getUpdatedAt());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthorId(post.getAuthorId());
        dto.setCommunityId(post.getCommunityId());
        dto.setStatus(post.getStatus());
        return dto;
    }

    @Override
    public PostListItemDto toListItemDto(Post post) {
        PostListItemDto dto = new PostListItemDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setCreatedAt(post.getCreatedAt());
        dto.setAuthorId(post.getAuthorId());
        dto.setStatus(post.getStatus());
        return dto;
    }
}
