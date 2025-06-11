package com.omniversity.server.user;

import com.omniversity.server.user.dto.DeleteUserDto;
import com.omniversity.server.user.dto.ExchangeUserRegistrationDto;
import com.omniversity.server.user.dto.ProspectiveUserRegistrationDto;
import com.omniversity.server.user.entity.User;
import com.omniversity.server.user.entity.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/register/exchange-user")
    ResponseEntity registerExchangeUser(@RequestBody ExchangeUserRegistrationDto exchangeUserRegistrationDTO) {
        try {
            return new ResponseEntity<>(this.userService.registerExchangeUser(exchangeUserRegistrationDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register/prospective-user")
    ResponseEntity registerProspectiveUser(@RequestBody ProspectiveUserRegistrationDto prospectiveUserRegistrationDto) {
        try {
            return new ResponseEntity<>(this.userService.registerProspectiveUser(prospectiveUserRegistrationDto), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    ResponseEntity deleteUserAccount(@RequestBody DeleteUserDto deleteUserDto) {
        try {
            return new ResponseEntity<>(this.userService.deleteUser(deleteUserDto), HttpStatus.ACCEPTED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
