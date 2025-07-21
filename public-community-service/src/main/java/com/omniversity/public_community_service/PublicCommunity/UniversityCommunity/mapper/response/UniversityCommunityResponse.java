package com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.mapper.response;

import com.omniversity.public_community_service.PublicCommunity.Entity.UniversityCommunity;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.response.UniversityCommunityIntroductionDto;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.response.UniversityCommunityPageDto;
import org.springframework.stereotype.Component;

@Component
public class UniversityCommunityResponse implements UniversityCommunityResponseMapper {
    @Override
    public UniversityCommunityPageDto toResponseUniversityCommunityDto(UniversityCommunity universityCommunity) {
        UniversityCommunityPageDto dto = new UniversityCommunityPageDto(
                universityCommunity.getName(),
                universityCommunity.getCreatedDate(),
                universityCommunity.getLogoImage(),
                universityCommunity.getBackgroundImage(),
                universityCommunity.getDescription(),
                universityCommunity.getCity(),
                universityCommunity.getCountry(),
                universityCommunity.getEmail(),
                universityCommunity.getWebsite(),
                universityCommunity.getPhoneNumber()
        );

        return dto;
    }

    @Override
    public UniversityCommunityIntroductionDto toUniversityCommunityIntroductionDto(UniversityCommunity universityCommunity) {
        UniversityCommunityIntroductionDto dto = new UniversityCommunityIntroductionDto(
                universityCommunity.getName(),
                universityCommunity.getBackgroundImage(),
                universityCommunity.getLogoImage(),
                universityCommunity.getDescription()
        );

        return dto;
    }
}
