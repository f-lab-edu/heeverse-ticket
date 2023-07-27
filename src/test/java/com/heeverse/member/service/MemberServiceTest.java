package com.heeverse.member.service;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.heeverse.member.domain.entity.Member;
import com.heeverse.member.domain.mapper.MemberMapper;
import com.heeverse.member.dto.MemberRequestDto;
import com.heeverse.member.exception.DuplicatedMemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jeongheekim
 * @date 2023/07/25
 */
@ActiveProfiles("local")
@Transactional
@SpringBootTest
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberMapper memberMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private MemberRequestDto memberRequestDto;

    @DisplayName("회원_requestDto_생성")
    @BeforeEach
    void memberEntitySetUp() {
        memberService = new MemberService(passwordEncoder, memberMapper);
        memberRequestDto = MemberRequestDto.builder()
            .id("testerzzang")
            .password("!Test1234")
            .email("test@gmail.com")
            .userName("홍길동")
            .build();
    }

    @DisplayName("회원가입_성공_테스트")
    @Test
    void signUpSuccessTest() {
        memberService.signup(memberRequestDto);
        assertNotNull(memberMapper.findById(memberRequestDto.getId()));
    }

    @DisplayName("중복회원_회원가입_실패_테스트")
    @Test
    void duplication_signup_exception_test() {
        memberMapper.insertMember(Member
            .builder()
            .id(memberRequestDto.getId())
            .password(memberRequestDto.getPassword())
            .userName(memberRequestDto.getUserName())
            .email(memberRequestDto.getEmail())
            .build());
        assertThrows(DuplicatedMemberException.class, () -> memberService.signup(memberRequestDto));

    }

}