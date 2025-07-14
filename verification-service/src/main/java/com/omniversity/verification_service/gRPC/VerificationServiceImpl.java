package com.omniversity.verification_service.gRPC;

import com.omniversity.verification_service.grpc.VerificationRequest;
import com.omniversity.verification_service.grpc.VerificationResponse;
import com.omniversity.verification_service.grpc.VerificationServiceGrpc;
import com.omniversity.verification_service.service.ValidationHash;
import com.omniversity.verification_service.token.RegistrationTokenService;
import com.omniversity.verification_service.token.dto.VerificationDto;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class VerificationServiceImpl extends VerificationServiceGrpc.VerificationServiceImplBase {

    private RegistrationTokenService registrationTokenService;

    @Autowired
    private ValidationHash validationHash;

    public VerificationServiceImpl(RegistrationTokenService registrationTokenService, ValidationHash validationHash) {
        this.registrationTokenService = registrationTokenService;
        this.validationHash = validationHash;
    }
    @Override
    public void sendVerificationRequest(VerificationRequest request, StreamObserver<VerificationResponse> responseObserver) {
        Long id = request.getId();
        String email = request.getEmail();
        String code = request.getCode();
        int verificationType = request.getEmailType();

        try {
            VerificationDto dto = new VerificationDto(code, email, verificationType);

            if (registrationTokenService.verifyToken(dto)) {
                String hash = validationHash.generateHmac(dto.email());
                responseObserver.onNext(VerificationResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage(hash)
                        .build());
            } else {
                responseObserver.onNext(VerificationResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage("Set failed field")
                        .build());
            }
        } catch (Exception e) {
            responseObserver.onError(
                    io.grpc.Status.INTERNAL
                            .withDescription("An unexpected error occurred: " + e.getMessage())
                            .asRuntimeException()
            );
        }
        responseObserver.onCompleted();
    }

}
