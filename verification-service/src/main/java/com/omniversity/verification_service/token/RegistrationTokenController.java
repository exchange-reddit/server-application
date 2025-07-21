package com.omniversity.verification_service.token;

import com.omniversity.verification_service.token.entity.RegistrationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.omniversity.verification_service.token.dto.RegistrationDto;
import com.omniversity.verification_service.token.dto.VerificationDto;

// All endpoints for this method is set as /token
@RestController
@RequestMapping("/token")
public class RegistrationTokenController {

    private RegistrationTokenService registrationTokenService;

    @Autowired
    public RegistrationTokenController(RegistrationTokenService registrationTokenService) {
        this.registrationTokenService = registrationTokenService;
    }

    @GetMapping("/email")
    public ResponseEntity requestTokenEmail(@RequestBody RegistrationDto registrationDto) {
        try {
            // Check if there were any previous tokens created for the following email (To see further, check the relevant method in registrationTokenService)
            registrationTokenService.checkDuplicate(registrationDto.email());

            // Create the token based on the provided information
            RegistrationToken registrationToken = registrationTokenService.createRegistrationToken(registrationDto);

            // Send token through email
            registrationTokenService.sendRegistrationToken(registrationDto.name(), registrationDto.email(), registrationToken);

            // If email is sent to the user, return success message with 200 status
            return new ResponseEntity<>("Email has been sent successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            // If failure to send email, return failure message with 400 status
            return new ResponseEntity<>("An error occurred while performing your request", HttpStatus.BAD_REQUEST);
        }
    }

}
