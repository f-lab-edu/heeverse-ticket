package com.heeverse.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gutenlee
 * @since 2023/07/23
 */
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        authentication.getName();
        authentication.getCredentials();

        // TODO : Member 조회, 비밀번호 검사 로직 구현하기

        return new UsernamePasswordAuthenticationToken(authentication.getName(), authentication.getCredentials(), getAuthority());
    }

    private List<GrantedAuthority> getAuthority() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
