package com.omniversity.public_community_service.PublicCommunity.AbstractCommunity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/communities")
public class AbstractCommunityController {
    private final AbstractCommunityService abstractCommunityService;

    @Autowired
    public AbstractCommunityController (AbstractCommunityService abstractCommunityService) {
        this.abstractCommunityService = abstractCommunityService;
    }
}
