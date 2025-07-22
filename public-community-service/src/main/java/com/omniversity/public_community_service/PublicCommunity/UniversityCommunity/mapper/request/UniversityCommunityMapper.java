package com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.mapper.request;

import com.omniversity.public_community_service.PublicCommunity.Entity.UniversityCommunity;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.request.UniversityCommunityCreationDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UniversityCommunityMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    UniversityCommunity toEntity(UniversityCommunityCreationDto dto);
}
