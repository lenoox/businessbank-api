package com.lenoox.businessbank.user.service;

import com.lenoox.businessbank.common.util.JwtTokenCreator;
import com.lenoox.businessbank.common.util.JwtTokenValidator;
import com.lenoox.businessbank.user.User;
import com.lenoox.businessbank.user.UserMapper;
import com.lenoox.businessbank.user.UserRepository;
import com.lenoox.businessbank.user.dto.JwtRequest;
import com.lenoox.businessbank.user.dto.JwtResponse;
import com.lenoox.businessbank.user.dto.UserRequest;
import com.lenoox.businessbank.user.dto.UserResponse;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final JwtTokenValidator jwtTokenValidator;
    private final JwtTokenCreator jwtTokenCreator;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, JwtTokenValidator jwtTokenValidator, JwtTokenCreator jwtTokenCreator) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jwtTokenValidator = jwtTokenValidator;
        this.jwtTokenCreator = jwtTokenCreator;
    }

    public UserResponse register(UserRequest userRequest) {
        User user = userMapper.requestToEntity(userRequest);
        User userEntity = userRepository.save(user);
        UserResponse userResponse = userMapper.entityToResponse(userEntity);
        return userResponse;
    }

    public JwtResponse login() {
        return jwtTokenCreator.generateToken();
    }

    public JwtResponse refreshToken(JwtRequest jwtRequest, HttpServletRequest request) {
        jwtTokenValidator.validateJwtToken(request, jwtRequest.getRefreshToken(), true);
        return jwtTokenCreator.generateToken();
    }
}
