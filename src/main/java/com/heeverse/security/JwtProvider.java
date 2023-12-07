package com.heeverse.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Authentication is passed into the AuthenticationManager to be authenticated
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final JwtDecoder jwtDecoder;


    public boolean validate(String token) throws AuthenticationException {
        log.info("JwtProvider");
        try {
            validateJwtToken(token);
            return true;
        } catch (Exception e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return false;
        }
    }


    public String generateToken(String principal, Authentication authentication) {
        Assert.notNull(authentication, "authentication은 not null입니다.");

        String auth = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        return jwtDecoder.issueJwtToken(principal, auth);

    }


    private Claims validateJwtToken(String token) {
        return jwtDecoder.toClaims(token);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtDecoder.toClaims(token);
        return new UsernamePasswordAuthenticationToken(claims.getId(), null, List.of(new SimpleGrantedAuthority(Authority.USER)));
    }
}
