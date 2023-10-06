package com.heeverse.common.factory;

import com.github.javafaker.Faker;
import com.heeverse.member.domain.MemberTestHelper;
import com.heeverse.member.domain.entity.Member;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.List;
import java.util.Locale;

/**
 * @author gutenlee
 * @since 2023/10/06
 */
public class WithCustomSecurityContextFactory implements WithSecurityContextFactory<WithMockMember> {
    private static Faker faker = MemberTestHelper.fakerLocale(Locale.ENGLISH);
    @Override
    public SecurityContext createSecurityContext(WithMockMember withMockMember) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("ROLE_USER");

        Member mockMember = new Member(faker.book().author(), withMockMember.password(), withMockMember.userName(), withMockMember.email());
        Authentication authentication = new UsernamePasswordAuthenticationToken(mockMember, null, authorities);
        context.setAuthentication(authentication);
        return context;
    }
}
