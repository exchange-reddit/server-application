package com.omniversity.public_community_service.PublicCommunity.AbstractCommunity;

import com.omniversity.public_community_service.PublicCommunity.AbstractCommunity.dto.update.CommunityUpdateBackgroundImageDto;
import com.omniversity.public_community_service.PublicCommunity.AbstractCommunity.dto.update.CommunityUpdateLogoImage;
import com.omniversity.public_community_service.PublicCommunity.Entity.AbstractCommunity;
import com.omniversity.public_community_service.PublicCommunity.Exception.CommunityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AbstractCommunityService {
    private final AbstractCommunityRepository abstractCommunityRepository;

    @Autowired
    public AbstractCommunityService (AbstractCommunityRepository abstractCommunityRepository) {
        this.abstractCommunityRepository = abstractCommunityRepository;
    }

    public AbstractCommunity getCommunityById (Long id) {
        AbstractCommunity community = abstractCommunityRepository.findById(id).orElseThrow(() ->
        {
            throw new CommunityNotFoundException("The requested community was not found.");
        });

        return community;
    }

    // Check existence of a community by name
    public boolean checkNameTaken (String name) {
        return this.abstractCommunityRepository.findByName(name).isPresent();
    }

    // Update logo image of a community
    public void updateCommunityLogoImage (CommunityUpdateLogoImage dto, AbstractCommunity community) {
        // Set new image as the logo image
        community.setLogoImage(dto.logoImage());
        abstractCommunityRepository.save(community);
    }

    // Update background image of a community
    public void updateCommunityBackgroundImage (CommunityUpdateBackgroundImageDto dto, AbstractCommunity community) {
        // Set new image as the background image
        community.setBackgroundImage(dto.backgroundImage());
        abstractCommunityRepository.save(community);
    }

}
