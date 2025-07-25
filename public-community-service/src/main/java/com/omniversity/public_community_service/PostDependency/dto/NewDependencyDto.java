package com.omniversity.public_community_service.PostDependency.dto;

public record NewDependencyDto(
        Long sectionId,
        Long postId,
        Long authorId
) {
}
