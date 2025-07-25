package com.omniversity.public_community_service.Section.PostSection;

import com.omniversity.public_community_service.PostDependency.PostDependencyEntity;
import com.omniversity.public_community_service.PostDependency.PostDependencyRepository;
import com.omniversity.public_community_service.PostDependency.dto.NewDependencyDto;
import com.omniversity.public_community_service.Section.Entity.PostSection;
import com.omniversity.public_community_service.Section.Exception.NoSuchSectionException;
import com.omniversity.public_community_service.Section.PostSection.dto.ThumbNailRetrievalDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostSectionService {
    private final PostSectionRepository postSectionRepository;
    private final PostDependencyRepository postDependencyRepository;

    @Autowired
    public PostSectionService(PostSectionRepository postSectionRepository, PostDependencyRepository postDependencyRepository) {
        this.postSectionRepository = postSectionRepository;
        this.postDependencyRepository = postDependencyRepository;
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
        List<Long> postIds = this.postDependencyRepository.findAllPosts(id);
        return postIds;
    }



}
