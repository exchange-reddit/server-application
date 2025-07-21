package com.omniversity.verification_service.gRPC;

import com.omniversity.verification_service.grpc.VerificationRequest;
import com.omniversity.verification_service.grpc.VerificationResponse;
import com.omniversity.verification_service.grpc.VerificationServiceGrpc;
import com.omniversity.verification_service.service.ValidationHash;
import com.omniversity.verification_service.token.RegistrationTokenService;
import com.omniversity.verification_service.token.dto.VerificationDto;
import com.omniversity.verification_service.token.exceptions.InvalidTokenException;
import com.omniversity.verification_service.token.exceptions.NoSuchTokenException;
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

    /**
     * Receive request from User Service via gRPC to validate registration token.
     * Returns a hash value created from the email that the user used to validate their account.
     * @param request
     * @param responseObserver
     */
    @Override
    public void sendVerificationRequest(VerificationRequest request, StreamObserver<VerificationResponse> responseObserver) {
        // Email that is being used for verification
        String email = request.getEmail();
        // The verification code that our server sent to the user
        String code = request.getCode();
        // The type of email that the user is trying to verify
        int verificationType = request.getEmailType();

        try {
            // Parse the provided information into a dto format
            VerificationDto dto = new VerificationDto(code, email, verificationType);

            try {
                // Run the token verification logic to validate the dto
                registrationTokenService.verifyToken(dto);

                // If no exceptions are thrown from the service layer, it indicates a success.
                // Create a validation hash from the email
                String hash = validationHash.generateHmac(dto.email());
                // Parse the result of the action into a proto and return
                responseObserver.onNext(VerificationResponse.newBuilder()
                        .setSuccess(true)
                        .setMessage(hash)
                        .setErrorMessage("")
                        .build());
            } catch (InvalidTokenException | NoSuchTokenException e) {
                // If an exception is thrown from the service layer, store the error message in the Error Message field of the proto
                responseObserver.onNext(VerificationResponse.newBuilder()
                        .setSuccess(false)
                        .setMessage(e.getMessage())
                        .setErrorMessage(e.getMessage())
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
