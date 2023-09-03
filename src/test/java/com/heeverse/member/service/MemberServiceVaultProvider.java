package com.heeverse.member.service;

import com.heeverse.VaultContainerProvider;
import com.heeverse.member.domain.mapper.MemberMapper;
import com.heeverse.member.dto.MemberRequestDto;
import com.heeverse.member.exception.DuplicatedMemberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * @author jeongheekim
 * @date 2023/07/25
 */
@ActiveProfiles("local")
@Transactional
@SpringBootTest
class MemberServiceTest extends VaultContainerProvider{

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberMapper memberMapper;

    private MemberRequestDto memberRequestDto;

    @DisplayName("회원 requestDto 생성")
    @BeforeEach
    void memberEntitySetUp() {
        memberRequestDto = new MemberRequestDto("testerzzang", "!Test1234", "홍길동",
            "test@gmail.com");
    }

    @DisplayName("회원가입 성공 테스트")
    @Test
    void 회원가입_성공() {
        memberService.signup(memberRequestDto);
        assertNotNull(memberMapper.findById(memberRequestDto.getId()).getSeq());
    }

    @DisplayName("중복회원 회원가입 실패 테스트")
    @Test
    void duplication_signup_exception_test() {

        회원가입_성공();

        assertThrows(DuplicatedMemberException.class, () -> memberService.signup(memberRequestDto));

    }

}