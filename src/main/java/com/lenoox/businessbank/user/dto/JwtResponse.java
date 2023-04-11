package com.lenoox.businessbank.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class JwtResponse {
    private String token;
    private String refreshToken;
}
