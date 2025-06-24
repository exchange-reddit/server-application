package com.omniversity.server.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RefreshTokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
