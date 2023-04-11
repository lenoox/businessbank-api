package com.lenoox.businessbank.common.security;

import com.lenoox.businessbank.common.filter.JwtTokenValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfiguration {
    private final JwtTokenValidationFilter jwtTokenValidationFilter;

    public SecurityConfiguration(JwtTokenValidationFilter jwtTokenValidationFilter) {
        this.jwtTokenValidationFilter = jwtTokenValidationFilter;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(jwtTokenValidationFilter, BasicAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers("/payments").authenticated()
                .antMatchers("/register").permitAll()
                .antMatchers("/login").permitAll()
                .and()
                .httpBasic().and().csrf().disable();
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
