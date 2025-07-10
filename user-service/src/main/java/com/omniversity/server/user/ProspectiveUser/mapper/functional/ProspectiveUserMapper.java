package com.omniversity.server.user.ProspectiveUser.mapper.functional;

import com.omniversity.server.user.ProspectiveUser.dto.request.ProspectiveUserRegistrationDto;
import com.omniversity.server.user.entity.ProspectiveUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProspectiveUserMapper {
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "id", ignore = true)
    ProspectiveUser toEntity(ProspectiveUserRegistrationDto dto);
}
