package com.omniversity.server.user.ExchangeUser;

import com.omniversity.server.user.ExchangeUser.dto.request.RegistrationDto;
import com.omniversity.server.user.exception.NoSuchUserException;
import com.omniversity.server.user.exception.UserAlreadyExistsException;
import com.omniversity.server.user.exception.WeakPasswordException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/exchange")
public class ExchangeUserController {
    private final ExchangeUserService exchangeUserService;

    @Autowired
    public ExchangeUserController(ExchangeUserService exchangeUserService) {
        this.exchangeUserService = exchangeUserService;
    }

    /**
     * @param id The unique id of the user
     * @return The user's public profile
     */
    @GetMapping("/profile/id/{id}")
    ResponseEntity getUserProfile(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(this.exchangeUserService.getPublicUserProfileId(id, 1), HttpStatus.OK);
        } catch (NoSuchUserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param userName The unique username of the user
     * @return The user's public profile
     */
    @GetMapping("/profile/username/{userName}")
    ResponseEntity getUserProfileUserName(@PathVariable String userName) {
        try {
            return new ResponseEntity<>(this.exchangeUserService.getPublicUserProfileUserName(userName), HttpStatus.OK);
        } catch (NoSuchUserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param id The unique id of the user
     * @return Part of the user's profile that is displayed for follow suggestions
     */
    @GetMapping("/recommendation/{id}")
    ResponseEntity getRecommendationProfile(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(this.exchangeUserService.getPublicUserProfileId(id, 2), HttpStatus.OK);
        } catch (NoSuchUserException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param dto Registration DTO that contains the personal information and credentials for new account registration
     * @return A new exchange user account
     */
    @PostMapping("/register")
    ResponseEntity registerExchangeUser(@RequestBody RegistrationDto dto) {
        try {
            return new ResponseEntity<>(this.exchangeUserService.registerExchangeUser(dto), HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (WeakPasswordException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
