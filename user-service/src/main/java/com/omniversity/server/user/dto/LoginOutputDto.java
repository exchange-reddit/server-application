package com.omniversity.server.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginOutputDto {
    private Long userId;
    private String email;
    private String accessToken;
    private String refreshToken;
}
