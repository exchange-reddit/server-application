package com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.response;

import com.omniversity.public_community_service.PublicCommunity.Entity.enums.City;
import com.omniversity.public_community_service.PublicCommunity.Entity.enums.Country;

import java.time.LocalDate;

public record UniversityCommunityPageDto(
    String name,
    LocalDate createdDate,
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
