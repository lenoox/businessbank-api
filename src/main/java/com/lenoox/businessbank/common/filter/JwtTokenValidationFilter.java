package com.lenoox.businessbank.common.filter;

import com.lenoox.businessbank.common.util.JwtTokenValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenValidationFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenValidator jwtTokenValidator;

    public JwtTokenValidationFilter(JwtTokenValidator jwtTokenValidator) {
        this.jwtTokenValidator = jwtTokenValidator;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        jwtTokenValidator.validateJwtToken(request, null, false);
        filterChain.doFilter(request, response);
    }
}
