package com.lenoox.businessbank.user;

import com.lenoox.businessbank.user.dto.UserRequest;
import com.lenoox.businessbank.user.dto.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    private final PasswordEncoder encoder;

    public UserMapper(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public User requestToEntity(UserRequest userRequest) {
        return User.builder()
                .username(userRequest.getUsername())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .password(encoder.encode(userRequest.getPassword()))
                .role(userRequest.getRole()).build();
    }

    public UserResponse entityToResponse(User userEntity) {
        return UserResponse.builder()
                .username(userEntity.getUsername())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .build();
    }
}
