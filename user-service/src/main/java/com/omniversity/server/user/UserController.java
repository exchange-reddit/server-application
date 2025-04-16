package com.omniversity.server.user;

import com.omniversity.server.user.dto.ExchangeUserRegistrationDTO;
import com.omniversity.server.user.entity.User;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users/exchange")
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

    @PostMapping("/register")
    ResponseEntity registerUser(@RequestBody ExchangeUserRegistrationDTO exchangeUserRegistrationDTO) {
        try {
            return new ResponseEntity<>(this.userService.registerUser(exchangeUserRegistrationDTO), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e, HttpStatus.BAD_REQUEST);
        }
    }
}
