package com.heeverse.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeverse.security.JsonAuthenticationFilter;
import com.heeverse.security.JwtAuthenticationFilter;
import com.heeverse.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * @author gutenlee
 * @since 2023/07/22
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;


    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.csrf(AbstractHttpConfigurer::disable)
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                BasicAuthenticationFilter.class)
            .addFilterAt(new JsonAuthenticationFilter(
                authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),
                jwtTokenProvider, objectMapper), UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests((request) -> request
                .requestMatchers("/login").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/member").permitAll()
                .anyRequest()
                .authenticated());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
        throws Exception {
        return authConfig.getAuthenticationManager();
    }
}
