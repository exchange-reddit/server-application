package com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.request;

import com.omniversity.public_community_service.PublicCommunity.Entity.enums.City;
import com.omniversity.public_community_service.PublicCommunity.Entity.enums.Country;

public record UniversityCommunityCreationDto(
        String name,
        String logoImage,
        String backgroundImage,
        String description,
        City city,
        Country country,
        String email,
        String website,
        String phoneNumber
) {
}
