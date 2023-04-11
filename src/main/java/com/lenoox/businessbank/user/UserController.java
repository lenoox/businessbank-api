package com.lenoox.businessbank.user;

import com.lenoox.businessbank.user.dto.JwtRequest;
import com.lenoox.businessbank.user.dto.JwtResponse;
import com.lenoox.businessbank.user.dto.UserRequest;
import com.lenoox.businessbank.user.dto.UserResponse;
import com.lenoox.businessbank.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public UserResponse registerUser(@RequestBody UserRequest userRequest) {
        return service.register(userRequest);
    }

    @PostMapping("/login")
    public JwtResponse login() {
        return service.login();
    }

    @GetMapping("/refresh")
    public JwtResponse refreshToken(@RequestBody JwtRequest jwtRequest, HttpServletRequest request) {
        return service.refreshToken(jwtRequest, request);
    }
}
