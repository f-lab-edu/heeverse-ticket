package com.heeverse.member.controller;

import com.heeverse.member.domain.entity.Member;
import com.heeverse.member.dto.MemberRequestDto;
import com.heeverse.member.service.MemberService;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @author gutenlee
 * @since 2023/07/26
 */
class StubMemberService extends MemberService {

    Map<String, Member> mockMembers;
    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public StubMemberService() {
        super(new BCryptPasswordEncoder(), null);
        mockMembers = createMockMember();
    }


    @Override
    public void signup(MemberRequestDto memberRequestDto) {
        return;
    }

    @Override
    public Optional<Member> findMember(String id) {
        if (StringUtils.isBlank(id)) {
            return Optional.empty();
        }
        return Optional.ofNullable(mockMembers.get(id));
    }


    private Map<String, Member> createMockMember() {
        Map<String, Member> mockMembers = new HashMap<>();

        mockMembers.put("okUser", Member.builder()
                                        .id("okUser")
                                        .password(passwordEncoder.encode("123"))
                                        .build());
        mockMembers.put("failUser", Member.builder()
                                        .id("failUser")
                                        .password("123")
                                        .build());
        return mockMembers;
    }

}
