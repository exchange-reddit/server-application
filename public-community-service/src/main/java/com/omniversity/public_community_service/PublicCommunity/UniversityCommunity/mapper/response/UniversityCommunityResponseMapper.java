package com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.mapper.response;

import com.omniversity.public_community_service.PublicCommunity.Entity.UniversityCommunity;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.response.UniversityCommunityIntroductionDto;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.response.UniversityCommunityPageDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UniversityCommunityResponseMapper {
    UniversityCommunityPageDto toResponseUniversityCommunityDto (UniversityCommunity universityCommunity);
    UniversityCommunityIntroductionDto toUniversityCommunityIntroductionDto (UniversityCommunity universityCommunity);
}
