package com.heeverse.common.factory;

import com.heeverse.member.domain.entity.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * @author gutenlee
 * @since 2023/10/06
 */
public class WithCustomSecurityContextFactory implements WithSecurityContextFactory<WithMockMember> {
    @Override
    public SecurityContext createSecurityContext(WithMockMember withMockMember) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");

        Authentication authentication = new UsernamePasswordAuthenticationToken(mock(Member.class), null, authorities);
        context.setAuthentication(authentication);
        return context;
    }
}
