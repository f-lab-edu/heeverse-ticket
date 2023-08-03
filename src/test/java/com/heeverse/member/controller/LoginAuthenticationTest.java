package com.heeverse.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeverse.member.dto.LoginRequestDto;
import com.heeverse.member.dto.MemberRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@Transactional
@SpringBootTest
class LoginAuthenticationTest {

    @Autowired
    private WebApplicationContext context;
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

        mvc.perform(post("/member")
                    .content(objectMapper.writeValueAsString(validMemberReqDto))
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());
    }


    @Test
    @DisplayName("로그인에 성공하면 HttpStatus 는 redirection 다")
    void when_login_success_should_return_OK() throws Exception {

        // given
        signUpTest();
        LoginRequestDto loginRequestDto = new LoginRequestDto(validMemberReqDto.getId(), validMemberReqDto.getPassword());

        mvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().is3xxRedirection());
    }


    @Test
    @DisplayName("로그인에 성공하는 경우 Authentication 의 타입은 UsernamePasswordAuthenticationToken 이다")
    void when_success_login_authentication_should_return_usernamePwdAuthToken() throws Exception {

        // given
        signUpTest();
        LoginRequestDto loginRequestDto = new LoginRequestDto(validMemberReqDto.getId(), validMemberReqDto.getPassword());

        // when
        ResultActions actions = mvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(loginRequestDto)));

        // then
        actions.andExpect(authenticated().withAuthentication(auth -> {
            assertInstanceOf(UsernamePasswordAuthenticationToken.class, auth);
        }));
    }


    @Test
    @WithAnonymousUser
    @DisplayName("로그인에 실패하면 Http Status 는 401 이다")
    void when_login_fail_should_return_401() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto(validMemberReqDto.getId(), validMemberReqDto.getPassword());

        mvc.perform(post("/login")
                    .content(objectMapper.writeValueAsString(loginRequestDto))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("익명의 USER 의 /member 요청은 인증,인가 permitAll 이므로 정상적으로 수행되어야 한다")
    @WithAnonymousUser
    void memeber_permitAll() throws Exception {

        mvc.perform(post("/member")
                    .content(objectMapper.writeValueAsString(validMemberReqDto))
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }


}

