package com.heeverse.member.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.heeverse.member.domain.entity.Member;
import com.heeverse.member.domain.mapper.MemberMapper;
import com.heeverse.member.dto.MemberRequestDto;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author jeongheekim
 * @date 2023/07/25
 */
class MemberServiceTest {

    @Test
    void signup() {
        //given
        MemberMapper memberMapper = mock(MemberMapper.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        MemberService memberService = new MemberService(passwordEncoder, memberMapper);
        MemberRequestDto requestDto = new MemberRequestDto();

        //when
        memberService.signup(requestDto);

        //then
        verify(memberMapper, times(1)).insertMember(any(Member.class));
    }
}