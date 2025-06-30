package com.omniversity.server.user.dto.response.jwt;

import java.util.Map;

import com.omniversity.server.user.dto.response.ReturnDto;

public record PublicKeyDto(Map<String, Object> key) implements ReturnDto {}
