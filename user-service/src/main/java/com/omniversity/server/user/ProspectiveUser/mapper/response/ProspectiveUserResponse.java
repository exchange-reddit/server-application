package com.omniversity.server.user.ProspectiveUser.mapper.response;
import com.omniversity.server.user.ProspectiveUser.dto.response.ProspectiveUserResponseNoPasswordDto;
import com.omniversity.server.user.entity.ProspectiveUser;
import org.springframework.stereotype.Component;

@Component
public class ProspectiveUserResponse implements ProspectiveUserResponseMapper {

    @Override
    public ProspectiveUserResponseNoPasswordDto toResponseProspectiveDto(ProspectiveUser prospectiveUser) {
        ProspectiveUserResponseNoPasswordDto dto = new ProspectiveUserResponseNoPasswordDto(
                prospectiveUser.getFirstName(),
                prospectiveUser.getLastName(),
                prospectiveUser.getPrivateEmail(),
                prospectiveUser.getUserName(),
                prospectiveUser.getDateOfBirth(),
                prospectiveUser.getHomeUni(),
                prospectiveUser.getDesiredUniversity(),
                prospectiveUser.getProgram(),
                prospectiveUser.getNationality(),
                prospectiveUser.getPreferredLanguage(),
                prospectiveUser.getProfilePicture()
        );
        return dto;
    }
}
