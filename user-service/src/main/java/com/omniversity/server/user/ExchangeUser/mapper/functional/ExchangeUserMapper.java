package com.omniversity.server.user.ExchangeUser.mapper.functional;

import com.omniversity.server.user.ExchangeUser.dto.request.RegistrationDto;
import com.omniversity.server.user.entity.ExchangeUser;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ExchangeUserMapper {

    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "id", ignore = true)
    ExchangeUser toEntity(RegistrationDto dto);

}
