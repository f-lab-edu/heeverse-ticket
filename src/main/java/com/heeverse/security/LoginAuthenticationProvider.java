package com.heeverse.security;

import com.heeverse.member.domain.entity.Member;
import com.heeverse.member.dto.AuthenticatedMember;
import com.heeverse.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author gutenlee
 * @since 2023/07/23
 */
@Slf4j
@Component
public class LoginAuthenticationProvider implements AuthenticationProvider {

    private final MemberService memberService;
    private final PasswordEncoder pwdEncoder;

    public LoginAuthenticationProvider(
            MemberService memberService,
            PasswordEncoder passwordEncoder
    ) {
        this.memberService = memberService;
        this.pwdEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        Member member = memberService.findMember(authentication.getName())
                .orElseThrow(() -> new AuthenticationServiceException("존재하지 않는 멤버입니다."));

        if (isMatched((String) authentication.getCredentials(), member.getPassword())) {
            return toUsernamePwdAuthenticationToken(new AuthenticatedMember(member.getSeq(), member.getId()));
        }

        throw new BadCredentialsException("인증에 실패했습니다");
    }

    private boolean isMatched(String inputPassword, String password) {
        return pwdEncoder.matches(inputPassword, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }


    private UsernamePasswordAuthenticationToken toUsernamePwdAuthenticationToken(AuthenticatedMember member) {
        return new UsernamePasswordAuthenticationToken(member, null, getAuthority());
    }


    private List<GrantedAuthority> getAuthority() {
        return List.of(new SimpleGrantedAuthority(Authority.USER));
    }

}
