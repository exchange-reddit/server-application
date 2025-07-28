package com.omniversity.server.gRPC.dto;


public record SimpleVerificationDto(
        boolean result,
        String message
) {
}
