package com.omniversity.server.gRPC;

import com.omniversity.server.gRPC.dto.VerificationDto;
import com.omniversity.verification_service.grpc.VerificationRequest;
import com.omniversity.verification_service.grpc.VerificationResponse;
import com.omniversity.verification_service.grpc.VerificationServiceGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

@Service
public class VerificationServiceClient {

    @GrpcClient("verification-service")
    private VerificationServiceGrpc.VerificationServiceBlockingStub blockingStub;

    public VerificationResponse sendVerificationRequest(VerificationDto dto) {
        VerificationRequest request = VerificationRequest.newBuilder()
                .setId(dto.id())
                .setEmail(dto.email())
                .setCode(dto.code())
                .setEmailType(dto.verificationType())
                .build();

        VerificationResponse response = blockingStub.sendVerificationRequest(request);

        return response;
    }
}
