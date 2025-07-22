package com.omniversity.public_community_service.PublicCommunity.UniversityCommunity;

import com.omniversity.public_community_service.PublicCommunity.AbstractCommunity.AbstractCommunityService;
import com.omniversity.public_community_service.PublicCommunity.Entity.UniversityCommunity;
import com.omniversity.public_community_service.PublicCommunity.Exception.CommunityNameTakenException;
import com.omniversity.public_community_service.PublicCommunity.Exception.CommunityNotFoundException;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.request.UniversityCommunityCreationDto;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.request.update.UniversityCommunityUpdateBackgroundImageDto;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.request.update.UniversityCommunityUpdateLogoDto;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.response.UniversityCommunityIntroductionDto;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.response.UniversityCommunityPageDto;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.mapper.request.UniversityCommunityMapper;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.mapper.response.UniversityCommunityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// TODO: Restrict ordinary users from creating, editing university communities (Probably through API gateway)
@Service
public class UniversityCommunityService {
    private final UniversityCommunityRepository universityCommunityRepository;
    private final UniversityCommunityResponse universityCommunityResponse;
    private final AbstractCommunityService abstractCommunityService;
    private final UniversityCommunityMapper universityCommunityMapper;

    @Autowired
    public UniversityCommunityService(UniversityCommunityRepository universityCommunityRepository, UniversityCommunityResponse universityCommunityResponse, AbstractCommunityService abstractCommunityService, UniversityCommunityMapper universityCommunityMapper) {
        this.universityCommunityRepository = universityCommunityRepository;
        this.universityCommunityResponse = universityCommunityResponse;
        this.abstractCommunityService = abstractCommunityService;
        this.universityCommunityMapper = universityCommunityMapper;
    }

    // Return University Community Object by id
    public UniversityCommunity getUniversityCommunityById (Long id) throws CommunityNotFoundException {
        // Query through db with provided id
        UniversityCommunity universityCommunity = universityCommunityRepository.findById(id).orElseThrow(() ->
        {
            throw new CommunityNotFoundException(String.format("Community with id %d was not found in the database.", id));
        });

        return universityCommunity;
    }

    // Retrieve entire University Community Page by id
    public UniversityCommunityPageDto getUniversityCommunityPageById (UniversityCommunity community) {
        // Map community object to response dto
        UniversityCommunityPageDto responseDto = universityCommunityResponse.toResponseUniversityCommunityDto(community);

        return responseDto;
    }

    // Retrieve University Introduction Page by id
    public UniversityCommunityIntroductionDto getUniversityCommunityIntroductionById (UniversityCommunity community) {
        // Map community object to response dto
        UniversityCommunityIntroductionDto responseDto = universityCommunityResponse.toUniversityCommunityIntroductionDto(community);

        return responseDto;
    }

    /**
     * Create University Community
     * A University Community shall only be created by the administrators (us).
     * Therefore, it is critical that we ensure a logic that validates the user creating a community is an administrator.
     */
    public UniversityCommunity createUniversityCommunity (UniversityCommunityCreationDto dto) throws CommunityNameTakenException {
        // Check if community name is taken or not
        if (abstractCommunityService.checkNameTaken(dto.name())) {
            throw new CommunityNameTakenException(String.format("Community name %s has been already taken by an another public community", dto.name()));
        }

        UniversityCommunity community = universityCommunityMapper.toEntity(dto);
        universityCommunityRepository.save(community);

        return community;
    }

}
