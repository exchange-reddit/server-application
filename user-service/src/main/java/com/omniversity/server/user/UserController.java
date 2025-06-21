package com.omniversity.server.user;

import com.omniversity.server.user.dto.ExchangeUserRegistrationDto;
import com.omniversity.server.user.dto.LoginInputDto;
import com.omniversity.server.user.dto.ProspectiveUserRegistrationDto;
import com.omniversity.server.user.dto.*;
import com.omniversity.server.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO:
 * - Guard condition to ensure user verification prior to registration
 * - Authentication method integration
 * - Add user posts and communities under account
 * - Function to secure the grace period of 2 weeks
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all")
    List<User> findAll() {
        return null;
    }

    @GetMapping("/{id}")
    ResponseEntity findById(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(id));
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/profile/{id}")
    ResponseEntity getUserProfile(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getPublicUserInfo(id, 1));
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/recommendation/{id}")
    ResponseEntity getRecommendationProfile(@PathVariable Integer id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(userService.getPublicUserInfo(id, 2));
        } catch (Exception e) {
            return new ResponseEntity(e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/checkID/{id}")
    ResponseEntity checkUserIdTaken(@PathVariable String id) {
        try {
            return new ResponseEntity<Boolean>(this.userService.checkUserIdTaken(id), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<Boolean>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    ResponseEntity loginUser(@RequestBody LoginInputDto loginDto) {
        try {
            return new ResponseEntity<>(this.userService.loginUser(loginDto));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
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

    @PutMapping("/update/{id}")
    ResponseEntity updateAccount(@PathVariable Integer id, @RequestBody UpdateAccountDto updateAccountDto) {
        try {
            Object updateResponseDto = userService.updateAccount(updateAccountDto, id);
            return ResponseEntity.status(HttpStatus.OK).body(updateResponseDto);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/pwChange/{id}")
    ResponseEntity changePW(@PathVariable Integer id, @RequestBody ChangePasswordDto changePasswordDto) {
        try {
            userService.changePassword(changePasswordDto, id);
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
