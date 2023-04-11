package com.lenoox.businessbank.common.util;

import com.lenoox.businessbank.common.config.GlobalProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtTokenValidator {
    private final GlobalProperties globalProperties;
    Logger logger = LoggerFactory.getLogger(JwtTokenValidator.class);

    @Autowired
    public JwtTokenValidator(GlobalProperties globalProperties) {
        this.globalProperties = globalProperties;
    }

    public void validateJwtToken(HttpServletRequest request, String refresh, boolean isRefreshValidation) {
        String authorizationHeaderValue = request.getHeader(SecurityContants.AUTHORIZATION_HEADER);
        if (authorizationHeaderValue != null && authorizationHeaderValue.startsWith("Bearer")) {
            String token = authorizationHeaderValue.substring(7);
            try {
                SecretKey key = Keys.hmacShaKeyFor(
                        this.globalProperties.getJwtKey().getBytes(StandardCharsets.UTF_8));

                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(isRefreshValidation ? refresh : token)
                        .getBody();

                String username = String.valueOf(claims.get("username"));
                String authorities = (String) claims.get("authorities");

                Authentication auth = new UsernamePasswordAuthenticationToken(username, null,
                        getUserRoles(authorities));

                SecurityContextHolder.getContext().setAuthentication(auth);
            } catch (ExpiredJwtException ex) {
                logger.info("Token expired!");

            } catch (Exception e) {
                logger.info(e.getMessage());
                throw new BadCredentialsException("Invalid Token received!");
            }
        }
    }

    private List<GrantedAuthority> getUserRoles(String authorities) {
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        String[] roles = authorities.split(",");
        for (String role : roles) {
            grantedAuthorityList.add(new SimpleGrantedAuthority(role.replaceAll("\\s+", "")));
        }
        return grantedAuthorityList;
    }
}
