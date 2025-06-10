package com.omniversity.server.service;

import com.omniversity.server.user.dto.ProspectiveUserRegistrationDto;
import com.omniversity.server.user.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ProspectiveUserMapper {

    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "userType", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toEntity(ProspectiveUserRegistrationDto dto);

}
