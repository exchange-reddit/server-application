package com.omniversity.public_community_service.PublicCommunity.UniversityCommunity;

import com.omniversity.public_community_service.PublicCommunity.AbstractCommunity.AbstractCommunityService;
import com.omniversity.public_community_service.PublicCommunity.Entity.UniversityCommunity;
import com.omniversity.public_community_service.PublicCommunity.Exception.CommunityNameTakenException;
import com.omniversity.public_community_service.PublicCommunity.Exception.CommunityNotFoundException;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.request.UniversityCommunityCreationDto;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.response.UniversityCommunityIntroductionDto;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.response.UniversityCommunityPageDto;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.mapper.request.UniversityCommunityMapper;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.mapper.response.UniversityCommunityResponse;
import com.omniversity.public_community_service.Section.Entity.PostSection;
import com.omniversity.public_community_service.Section.Exception.NoRelatedSectionsException;
import com.omniversity.public_community_service.Section.PostSection.PostSectionRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// TODO: Restrict ordinary users from creating, editing university communities (Probably through API gateway)
@Service
public class UniversityCommunityService {
    private final UniversityCommunityRepository universityCommunityRepository;
    private final PostSectionRepository postSectionRepository;
    private final UniversityCommunityResponse universityCommunityResponse;
    private final AbstractCommunityService abstractCommunityService;
    private final UniversityCommunityMapper universityCommunityMapper;

    @Autowired
    public UniversityCommunityService(UniversityCommunityRepository universityCommunityRepository, PostSectionRepository postSectionRepository, UniversityCommunityResponse universityCommunityResponse, AbstractCommunityService abstractCommunityService, UniversityCommunityMapper universityCommunityMapper) {
        this.universityCommunityRepository = universityCommunityRepository;
        this.postSectionRepository = postSectionRepository;
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

    @Transactional
    public UniversityCommunity addPostSection (Long id, PostSection postSection) throws CommunityNotFoundException {
        UniversityCommunity community = this.universityCommunityRepository.findById(id).orElseThrow(() ->
        {
            throw new CommunityNotFoundException(String.format("Community with id %d was not found", id));
        });

        community.addPostSection(postSection);
        return universityCommunityRepository.save(community);
    }

    // Returns the list of relevant post sections
    @Transactional(readOnly = true)
    public List<Long> getPostSections(Long id) {
        List<Long> communityIds = postSectionRepository.findByCommunityId(id).orElseThrow(() ->
        {
            throw new NoRelatedSectionsException(String.format("No related post sections were found for the community with id %d", id));
        });

        return communityIds;
    }


}
