package com.lenoox.businessbank.common.util;


import com.lenoox.businessbank.common.config.GlobalProperties;
import com.lenoox.businessbank.user.dto.JwtResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class JwtTokenCreator {
    private final GlobalProperties globalProperties;

    public JwtTokenCreator(GlobalProperties globalProperties) {
        this.globalProperties = globalProperties;
    }

    public JwtResponse generateToken() {
        String jwtToken = null, refreshToken = null;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            String username = authentication.getName();
            SecretKey key = Keys.hmacShaKeyFor(this.globalProperties.getJwtKey().getBytes(StandardCharsets.UTF_8));

            jwtToken = Jwts.builder()
                    .setIssuer("businessbank")
                    .setExpiration(new Date((new Date()).getTime() + 30000))
                    .setSubject("businessbank-token")
                    .claim("username", username)
                    .claim("authorities", getUserRoles((List<GrantedAuthority>) authentication.getAuthorities()))
                    .signWith(key)
                    .compact();

            refreshToken = Jwts.builder()
                    .setIssuer("businessbank")
                    .setExpiration(new Date((new Date()).getTime() + 3000000))
                    .setSubject("businessbank-token")
                    .claim("username", username)
                    .claim("authorities", getUserRoles((List<GrantedAuthority>) authentication.getAuthorities()))
                    .signWith(key)
                    .compact();

            JwtResponse jwtResponse = JwtResponse.builder()
                    .token(jwtToken)
                    .refreshToken(refreshToken).build();
            return jwtResponse;
        }
        return null;
    }

    private String getUserRoles(List<GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}
