package com.omniversity.server.user;

import java.util.List;

import com.omniversity.server.user.dto.*;
import com.omniversity.server.user.dto.request.RefreshTokenRequestDto;
import com.omniversity.server.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TODO: Guard condition to ensure user verification prior to registration
 * TODO: Add user posts and communities under account
 * TODO: Function to secure the grace period of 2 weeks
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    List<User> findAll() {
        return null;
    }

    @GetMapping
    ResponseEntity findById(@RequestHeader("X-User-Id") Integer userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(userId));
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/profile")
    ResponseEntity getUserProfile(@RequestHeader("X-User-Id") Integer userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getPublicUserInfo(userId, 1));
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/recommendation")
    ResponseEntity getRecommendationProfile(@RequestHeader("X-User-Id") Integer userId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getPublicUserInfo(userId, 2));
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // TODO: not sure if this method is still valid?
    @GetMapping("/checkID/{id}")
    ResponseEntity checkUserIdTaken(@PathVariable String id) {
        try {
            return new ResponseEntity<Boolean>(this.userService.checkUserIdTaken(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Returns public key to be used by cloud gateway to verify the jwt tokens
     *
     */
    @GetMapping("/.well-known/jwks.json")
    ResponseEntity getKey() {
        try {
            return new ResponseEntity<>(this.userService.getPublicKey(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    ResponseEntity loginUser(@RequestBody LoginInputDto loginDto) {
        try {
            return new ResponseEntity<>(this.userService.loginUser(loginDto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("Logged out.");
    }


    /**
     * Refreshes access and refresh tokens. To be used when access token expires.
     * When only the access token is expired, both tokens are renewed and returned.
     * When both are expired, the method should reject, which should prompt the client to force logout.
     * By allowing both the access and refresh tokens to be renewed, we can expect for less force-logouts,
     *   leading to better ux.
     *
     * @param refreshTokenRequestDto
     * @return
     */
    @PostMapping("/refresh")
    ResponseEntity refreshToken(@RequestBody RefreshTokenRequestDto refreshTokenRequestDto) {
        try {
            return new ResponseEntity<>(this.userService.refreshToken(refreshTokenRequestDto), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/register/exchange-user")
    ResponseEntity registerExchangeUser(@RequestBody ExchangeUserRegistrationDto exchangeUserRegistrationDTO) {
        try {
            return new ResponseEntity<>(this.userService.registerExchangeUser(exchangeUserRegistrationDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/register/prospective-user")
    ResponseEntity registerProspectiveUser(@RequestBody ProspectiveUserRegistrationDto prospectiveUserRegistrationDto) {
        try {
            return new ResponseEntity<>(this.userService.registerProspectiveUser(prospectiveUserRegistrationDto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update")
    ResponseEntity updateAccount(
            @RequestHeader("X-User-Id") Integer userId,
            @RequestBody UpdateAccountDto updateAccountDto
    ) {
        try {
            Object updateResponseDto = userService.updateAccount(updateAccountDto, userId);
            return ResponseEntity.status(HttpStatus.OK).body(updateResponseDto);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/pwChange")
    ResponseEntity changePW(
            @RequestHeader("X-User-Id") Integer userId,
            @RequestBody ChangePasswordDto changePasswordDto
    ) {
        try {
            userService.changePassword(changePasswordDto, userId);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    ResponseEntity deleteUserAccount(@RequestBody DeleteUserDto deleteUserDto) {
        try {
            Boolean result = this.userService.deleteUser(deleteUserDto);

            if (result) {
                return ResponseEntity.status(HttpStatus.ACCEPTED).body("Your account has been successfully deleted!");
            }
            else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An unknown error occurred.");
            }
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
