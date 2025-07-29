package com.omniversity.public_community_service.Section.PostSection;

import com.omniversity.public_community_service.SectionPostDependency.PostSectionDependencyRepository;
import com.omniversity.public_community_service.Section.Entity.PostSection;
import com.omniversity.public_community_service.Section.Exception.NoSuchSectionException;
import com.omniversity.public_community_service.Section.PostSection.dto.ThumbNailRetrievalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostSectionService {
    private final PostSectionRepository postSectionRepository;
    private final PostSectionDependencyRepository postSectionDependencyRepository;

    @Autowired
    public PostSectionService(PostSectionRepository postSectionRepository, PostSectionDependencyRepository postSectionDependencyRepository) {
        this.postSectionRepository = postSectionRepository;
        this.postSectionDependencyRepository = postSectionDependencyRepository;
    }

    public ThumbNailRetrievalDto getSectionThumbnailPosts(Long id) throws NoSuchSectionException {
        PostSection postSection = this.postSectionRepository.findById(id).orElseThrow(() ->
        {
            throw new NoSuchSectionException(String.format("The section with the following id was not found: %d", id));
        });

        ThumbNailRetrievalDto dto = new ThumbNailRetrievalDto(
                postSection.getThumbNailPostOne(),
                postSection.getThumbNailPostTwo(),
                postSection.getThumbNailPostThree()
        );

        return dto;
    }

    public List<Long> getAllPosts(Long id) {
        List<Long> postIds = this.postSectionDependencyRepository.findAllPostIds(id);
        return postIds;
    }



}
