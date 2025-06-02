package com.omniversity.server;

import org.springframework.web.bind.annotation.*;

@RestController // This annotation marks the class as a REST controller
@RequestMapping("/users")
public class UserController {
    @GetMapping
    public String getAllUsers() {
        return "Hello from User Service: List of all users!";
    }

    @GetMapping("/login") // This maps HTTP GET requests to /users/login
    public String loginUser(@RequestHeader("X-User-Id") String userId) {
        return "You have successfully reached the login API: " + userId;
    }

    @PostMapping("/signup")
    public String signupUser(@RequestHeader("X-User-Id") String userId) {
        return "You have successfully reached the signup API: " + userId;
    }
}
