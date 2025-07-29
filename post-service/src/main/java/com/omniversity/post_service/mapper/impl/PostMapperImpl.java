package com.omniversity.post_service.mapper.impl;

import com.omniversity.post_service.exception.custom.InvalidInputException;
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
        // Basic null/empty checks
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            throw new InvalidInputException("Post title cannot be empty.");
        }
        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            throw new InvalidInputException("Post content cannot be empty.");
        }
        if (dto.getAuthorId() == null) {
            throw new InvalidInputException("Author ID cannot be null.");
        }
        if (dto.getSectionId() == null) {
            throw new InvalidInputException("Section ID cannot be null.");
        }

        Post post = new Post();
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());
        post.setAuthorId(dto.getAuthorId());
        post.setSectionId(dto.getSectionId());
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
        if (dto.getSectionId() != null) {
            post.setSectionId(dto.getSectionId());
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
        dto.setCommunityId(post.getSectionId());
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
