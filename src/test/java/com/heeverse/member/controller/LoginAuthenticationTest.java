package com.heeverse.member.controller;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeverse.member.domain.mapper.MemberMapper;
import com.heeverse.member.dto.LoginRequestDto;
import com.heeverse.member.dto.MemberRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

@ActiveProfiles("local")
@Transactional
@SpringBootTest
class LoginAuthenticationTest {

    private final String LOGIN_URI = "/login";
    private final String SIGN_UP_URI = "/member";

    @Autowired
    private WebApplicationContext context;
    @Autowired
    private MemberMapper memberMapper;
    private MockMvc mvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private MemberRequestDto validMemberReqDto;



    @BeforeEach
    public void setup() {

        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        validMemberReqDto = new MemberRequestDto("heeverse12", "abcd1234!", "name", "email@naver.com");
    }


    @Test
    @DisplayName("회원가입 성공 테스트")
    public void signUpTest() throws Exception {

        getMemberSignUpAction(SIGN_UP_URI, validMemberReqDto)
                .andDo(print())
                .andExpect(status().isCreated());
    }


    @Test
    @DisplayName("[로그인 성공] HttpStatus 는 redirection 다")
    void when_login_success_should_return_OK() throws Exception {

        // given
        signUpTest();
        LoginRequestDto loginRequestDto = toLoginRequestDto(validMemberReqDto);

        getLoginAction(LOGIN_URI, loginRequestDto)
                .andExpect(status().is3xxRedirection());
    }


    @Test
    @DisplayName("[로그인 성공] Authentication 의 타입은 UsernamePasswordAuthenticationToken 이다")
    void when_success_login_authentication_should_return_usernamePwdAuthToken() throws Exception {

        // given
        signUpTest();
        LoginRequestDto loginRequestDto = toLoginRequestDto(validMemberReqDto);

        // when
        getLoginAction(LOGIN_URI, loginRequestDto)
            .andExpect(authenticated().withAuthentication(auth ->
                assertInstanceOf(UsernamePasswordAuthenticationToken.class, auth))
            );
    }


    @Test
    @WithAnonymousUser
    @DisplayName("[로그인 실패] 미등록 회원일 경우 Http Status 는 401 이다")
    void when_login_fail_should_return_401() throws Exception {

        LoginRequestDto loginRequestDto = toLoginRequestDto(validMemberReqDto);
        assertNull(memberMapper.findById(loginRequestDto.id()));

        getLoginAction(LOGIN_URI, loginRequestDto)
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("[로그인 실패] 비밀번호 불일치 경우 Http Status 는 401 이다")
    void when_login_fail_by_password_should_return_401() throws Exception {

        signUpTest();
        LoginRequestDto loginRequestDto = new LoginRequestDto(validMemberReqDto.getId(), "wrong_password");

        getLoginAction(LOGIN_URI, loginRequestDto)
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }



    @Test
    @DisplayName("익명의 USER 의 /member 요청은 인증,인가 permitAll 이므로 정상적으로 수행되어야 한다")
    @WithAnonymousUser
    void memeber_permitAll() throws Exception {

        getMemberSignUpAction(SIGN_UP_URI, validMemberReqDto)
                .andExpect(status().isCreated());
    }


    LoginRequestDto toLoginRequestDto(MemberRequestDto memberRequestDto) {
        return new LoginRequestDto(memberRequestDto.getId(), memberRequestDto.getPassword());
    }


    ResultActions getLoginAction(String uri, LoginRequestDto loginRequestDto) {
        try {
            return mvc.perform(
                        post(uri).content(objectMapper.writeValueAsString(loginRequestDto)));
        } catch (Exception e) {
            throw new IllegalArgumentException(loginRequestDto.toString());
        }
    }


    ResultActions getMemberSignUpAction(String uri, MemberRequestDto memberRequestDto) {
        try {
            return mvc.perform(post(uri)
                    .content(objectMapper.writeValueAsString(memberRequestDto))
                    .contentType(MediaType.APPLICATION_JSON));
        } catch (Exception e) {
            throw new IllegalArgumentException(memberRequestDto.toString());
        }
    }
}

