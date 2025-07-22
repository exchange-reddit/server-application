package com.omniversity.public_community_service.PublicCommunity.AbstractCommunity;

import com.omniversity.public_community_service.PublicCommunity.AbstractCommunity.dto.update.CommunityUpdateBackgroundImageDto;
import com.omniversity.public_community_service.PublicCommunity.AbstractCommunity.dto.update.CommunityUpdateLogoImage;
import com.omniversity.public_community_service.PublicCommunity.Entity.AbstractCommunity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/communities")
public class AbstractCommunityController {
    private final AbstractCommunityService abstractCommunityService;

    @Autowired
    public AbstractCommunityController (AbstractCommunityService abstractCommunityService) {
        this.abstractCommunityService = abstractCommunityService;
    }

    @PatchMapping("/update/{id}/logo")
    ResponseEntity updateCommunityLogo(@RequestBody CommunityUpdateLogoImage dto, @RequestParam Long id) {
        try {
            AbstractCommunity community = abstractCommunityService.getCommunityById(id);
            this.abstractCommunityService.updateCommunityLogoImage(dto, community);
            return new ResponseEntity<>("Successfully updated logo image", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/update/{id}/backgroundImage")
    ResponseEntity updateCommunityBackground(@RequestBody CommunityUpdateBackgroundImageDto dto, @RequestParam Long id) {
        try {
            AbstractCommunity community = abstractCommunityService.getCommunityById(id);
            this.abstractCommunityService.updateCommunityBackgroundImage(dto, community);
            return new ResponseEntity<>("Successfully updated background image", HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
