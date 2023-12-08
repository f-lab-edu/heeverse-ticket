package com.heeverse.security;

import com.heeverse.member.dto.AuthenticatedMember;
import com.heeverse.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

import static com.heeverse.security.ClaimConstants.MEMBER_SEQ;

/**
 * Authentication is passed into the AuthenticationManager to be authenticated
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtProvider {

    private final JwtDecoder jwtDecoder;
    private final MemberService memberService;


    public boolean validate(String token) throws AuthenticationException {
        try {
            validateJwtToken(token);
            return true;
        } catch (UnsupportedJwtException | MalformedJwtException | SignatureException
                 | AuthenticationServiceException | IllegalArgumentException e) {
            log.info(e.getMessage());
            throw new BadCredentialsException("인증에 실패했습니다");
        }
    }


    public String generateToken(Authentication authentication) {
        Assert.notNull(authentication, "authentication은 not null입니다.");
        return jwtDecoder.generateJwtToken((AuthenticatedMember) authentication.getPrincipal(), authentication.getAuthorities());
    }

    private void validateJwtToken(String token) {
        jwtDecoder.toClaims(token);
    }

    public Authentication getAuthentication(String token) {
        Claims claims = jwtDecoder.toClaims(token);
        AuthenticatedMember authenticatedMember = new AuthenticatedMember(Long.parseLong((String) claims.get(MEMBER_SEQ)), claims.getId());
        return new UsernamePasswordAuthenticationToken(authenticatedMember, null, getAuthorities());
    }

    private static List<GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(Authority.USER));
    }
}
