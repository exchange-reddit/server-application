package com.omniversity.public_community_service.Section.PostSection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/sections")
public class PostSectionController {
    private final PostSectionService postSectionService;

    @Autowired
    public PostSectionController (PostSectionService postSectionService) {
        this.postSectionService = postSectionService;
    }

    @GetMapping("/{id}/all-posts")
    ResponseEntity getAllPostsFromSection(@PathVariable Long id) {
        try {
            List<Long> postIds = this.postSectionService.getAllPosts(id);
            return new ResponseEntity<>(postIds, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Method to return the first N posts
}
