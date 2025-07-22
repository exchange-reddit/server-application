package com.omniversity.public_community_service.PublicCommunity.UniversityCommunity;

import com.omniversity.public_community_service.PublicCommunity.Entity.UniversityCommunity;
import com.omniversity.public_community_service.PublicCommunity.Exception.CommunityNameTakenException;
import com.omniversity.public_community_service.PublicCommunity.Exception.CommunityNotFoundException;
import com.omniversity.public_community_service.PublicCommunity.UniversityCommunity.dto.request.UniversityCommunityCreationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/community/universities")
public class UniversityCommunityController {
    private final UniversityCommunityService universityCommunityService;

    @Autowired
    public UniversityCommunityController (UniversityCommunityService universityCommunityService) {
        this.universityCommunityService = universityCommunityService;
    }

    @GetMapping("/community-page/{id}")
    ResponseEntity getUniversityCommunityPage(@PathVariable Long id) {
        try {
            UniversityCommunity universityCommunity = this.universityCommunityService.getUniversityCommunityById(id);
            return new ResponseEntity<>(this.universityCommunityService.getUniversityCommunityPageById(universityCommunity), HttpStatus.OK);
        } catch (CommunityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/introduction-page/{id}")
    ResponseEntity getUniversityCommunityIntroduction(@PathVariable Long id) {
        try {
            UniversityCommunity universityCommunity = this.universityCommunityService.getUniversityCommunityById(id);
            return new ResponseEntity<>(this.universityCommunityService.getUniversityCommunityIntroductionById(universityCommunity), HttpStatus.OK);
        } catch (CommunityNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    ResponseEntity createUniversityCommunity(@RequestBody UniversityCommunityCreationDto universityCommunityCreationDto) {
        try {
            this.universityCommunityService.createUniversityCommunity(universityCommunityCreationDto);
            return new ResponseEntity<>("New community has been created!", HttpStatus.CREATED);
        } catch (CommunityNameTakenException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
