package com.omniversity.post_service.mapper;

import com.omniversity.post_service.dto.output.PostDto;
import com.omniversity.post_service.entity.Post;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostMapper {
    PostDto toDto(Post post);
    Post toEntity(PostDto postDto);
    List<PostDto> toDtoList(List<Post> posts);
}