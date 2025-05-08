package com.omniversity.verification_service.token;

import com.omniversity.verification_service.token.entity.RegistrationToken;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/token")
public class RegistrationTokenController {

    private RegistrationTokenService registrationTokenService;

    @Autowired
    public RegistrationTokenController(RegistrationTokenService registrationTokenService) {
        this.registrationTokenService = registrationTokenService;
    }

    @GetMapping("/email")
    public ResponseEntity requestTokenEmail(@RequestBody String userEmail, String name, int option) {
        try {
            // Create the token based on the provided information
            RegistrationToken registrationToken = registrationTokenService.createRegistrationToken(userEmail, option);

            // Send token through email
            registrationTokenService.sendRegistrationToken(name, userEmail, option, registrationToken);

            return new ResponseEntity<>("Email has been sent successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("An error occurred while performing your request", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestBody String userEmail, String verificationCode, int option) {
        try {
            // Find token using email and verification code
            boolean result = registrationTokenService.verifyToken(verificationCode, userEmail, option);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }

}
