package com.omniversity.server.service.Mapper;

import com.omniversity.server.user.dto.ExchangeUserRegistrationDto;
import com.omniversity.server.user.entity.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ExchangeUserMapper {

    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "userType", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "id", ignore = true)
    User toEntity(ExchangeUserRegistrationDto dto);

}
