package com.lenoox.businessbank.user.service;

import com.lenoox.businessbank.user.dto.JwtRequest;
import com.lenoox.businessbank.user.dto.JwtResponse;
import com.lenoox.businessbank.user.dto.UserRequest;
import com.lenoox.businessbank.user.dto.UserResponse;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    UserResponse register(UserRequest userRequest);

    JwtResponse login();

    JwtResponse refreshToken(JwtRequest jwtRequest, HttpServletRequest request);
}
