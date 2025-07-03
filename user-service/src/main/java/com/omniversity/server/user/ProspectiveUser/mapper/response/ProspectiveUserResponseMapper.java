package com.omniversity.server.user.ProspectiveUser.mapper.response;

import com.omniversity.server.user.ProspectiveUser.dto.response.ProspectiveUserResponseNoPasswordDto;
import com.omniversity.server.user.entity.ProspectiveUser;

public interface ProspectiveUserResponseMapper {
    ProspectiveUserResponseNoPasswordDto toResponseProspectiveDto (ProspectiveUser prospectiveUser);
}
