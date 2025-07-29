package com.omniversity.public_community_service.Section.PostSection.dto.request;

public record FirstNPostsDto(
        int start,
        int end,
        Long sectionId
) {
}
