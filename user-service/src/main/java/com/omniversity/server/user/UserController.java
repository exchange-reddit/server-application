package com.omniversity.server.user;

import com.omniversity.server.log.LogService;
import com.omniversity.server.log.dto.accountDeleteLogDto;
import com.omniversity.server.log.dto.pwChangeLogDto;
import com.omniversity.server.user.dto.*;
import com.omniversity.server.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
    private LogService logService;

    @Autowired
    public UserController(UserService userService, LogService logService) {
        this.userService = userService;
        this.logService = logService;
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
    ResponseEntity changePW(@PathVariable Integer id, @RequestBody ChangePasswordDto changePasswordDto, HttpServletRequest request) {
        try {
            // Please check the method changePassword() for further information
            pwChangeLogDto dto = userService.changePassword(changePasswordDto, id);
            // Log pw change request information (The dto here is the result of the changePassword method, and the request is the entire request sent by the user)
            logService.logPWChange(dto, request);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/nationalityChange/{id}")
    ResponseEntity changeNationality(@PathVariable Integer id, @RequestBody ChangeNationalityDto dto) {
        try {
            userService.changeNationality(id, dto);
            return ResponseEntity.status(HttpStatus.OK).body("Your nationality has been changed successfully");
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete")
    ResponseEntity deleteUserAccount(@RequestBody DeleteUserDto deleteUserDto, HttpServletRequest request) {
        try {
            accountDeleteLogDto dto = this.userService.deleteUser(deleteUserDto);
            // Log account deletion information (The dto here is the result of the deleteUser method)
            logService.logAccountDeletion(dto, request);
            return ResponseEntity.status(200).body(String.format("Account ID %s has been terminated as of %s", dto.updateUser(), LocalDate.now()));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
