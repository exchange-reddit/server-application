package com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.mapper.request;

import com.omniversity.public_community_service.PublicCommunity.Entity.UniversityCommunity;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.request.UniversityCommunityCreationDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UniversityCommunityMapper {
    UniversityCommunity toEntity(UniversityCommunityCreationDto dto);
}
