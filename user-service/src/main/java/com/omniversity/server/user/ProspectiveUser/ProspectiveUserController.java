package com.omniversity.server.user.ProspectiveUser;

import com.omniversity.server.gRPC.VerificationServiceClient;
import com.omniversity.server.gRPC.dto.SimpleVerificationDto;
import com.omniversity.server.gRPC.dto.VerificationDto;
import com.omniversity.server.user.ProspectiveUser.dto.request.DesiredUniversityUpdateDto;
import com.omniversity.server.user.ProspectiveUser.dto.request.ProspectiveUserRegistrationDto;
import com.omniversity.server.user.exception.NoSuchUserException;
import com.omniversity.server.user.exception.UserAlreadyExistsException;
import com.omniversity.server.user.exception.WeakPasswordException;
import com.omniversity.verification_service.grpc.VerificationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/prospective")
public class ProspectiveUserController {
    private final ProspectiveUserService prospectiveUserService;
    private final VerificationServiceClient verificationServiceClient;

    @Autowired
    public ProspectiveUserController(ProspectiveUserService prospectiveUserService, VerificationServiceClient verificationServiceClient) {
        this.prospectiveUserService = prospectiveUserService;
        this.verificationServiceClient = verificationServiceClient;
    }

    @PostMapping("/register")
    ResponseEntity registerProspectiveUser(@RequestBody ProspectiveUserRegistrationDto dto) {
        try {
            return new ResponseEntity<>(this.prospectiveUserService.registerProspectiveUser(dto), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (WeakPasswordException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}/desiredUni")
    ResponseEntity updateDesiredUniversity(@PathVariable Long id, @RequestBody DesiredUniversityUpdateDto dto) {
        try {
            return new ResponseEntity<>(this.prospectiveUserService.updateDesiredExchangeUniversity(dto, id), HttpStatus.OK);
        } catch (NoSuchUserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/validate")
    ResponseEntity validateEmail(@RequestBody VerificationDto dto) {
        try {
            // Receive the validation result from verification-service
            VerificationResponse response = verificationServiceClient.sendVerificationRequest(dto);

            SimpleVerificationDto resultDto = new SimpleVerificationDto(
                    response.getSuccess(), response.getMessage()
            );

            return resultDto.result()
                    ? ResponseEntity.ok(resultDto)
                    : ResponseEntity.status(HttpStatus.FORBIDDEN).body(resultDto);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
