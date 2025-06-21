package com.omniversity.post_service.mapper;

import com.omniversity.post_service.dto.input.PostCreateDto;
import com.omniversity.post_service.entity.Post;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-17T16:41:56+0900",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.5.jar, environment: Java 17.0.14 (Homebrew)"
)
@Component
public class PostCreateMapperImpl implements PostCreateMapper {

    @Override
    public Post toEntity(PostCreateDto postCreateDto) {
        if ( postCreateDto == null ) {
            return null;
        }

        Post post = new Post();

        post.setTitle( postCreateDto.getTitle() );
        post.setContent( postCreateDto.getContent() );
        post.setAuthorId( postCreateDto.getAuthorId() );
        post.setCommunityId( postCreateDto.getCommunityId() );

        return post;
    }
}
