package com.omniversity.post_service.mapper;

import com.omniversity.post_service.dto.input.PostCreateDto;
import com.omniversity.post_service.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostCreateMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Post toEntity(PostCreateDto postCreateDto);
}

