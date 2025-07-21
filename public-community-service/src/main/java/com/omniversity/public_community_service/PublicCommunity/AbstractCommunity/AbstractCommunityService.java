package com.omniversity.public_community_service.PublicCommunity.AbstractCommunity;

import com.omniversity.public_community_service.PublicCommunity.Entity.AbstractCommunity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
            throw new RuntimeException();
        });

        return community;
    }

    // Check existence of a community by name
    public boolean checkNameTaken (String name) {
        return this.abstractCommunityRepository.findByName(name).isPresent();
    }

}
