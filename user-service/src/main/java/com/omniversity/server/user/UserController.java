package com.omniversity.server.user;

import com.omniversity.server.exception.NoSuchUserException;
import com.omniversity.server.exception.WrongPasswordException;
import com.omniversity.server.user.dto.*;
import com.omniversity.server.user.entity.User;
import com.omniversity.server.user.entity.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * TODO:
 * - Endpoint to edit user accounts
 * - Endpoint to reset user password
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
    User findById(@PathVariable Integer id) {
        return null;
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
