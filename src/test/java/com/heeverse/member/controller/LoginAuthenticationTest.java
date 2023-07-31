package com.heeverse.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeverse.member.dto.LoginRequestDto;
import com.heeverse.member.dto.MemberRequestDto;
import com.heeverse.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
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
    @Autowired
    private MemberService memberService;
    private MockMvc mvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    private MemberRequestDto memberRequestDto;


    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();

        setUpMember();
    }

    MemberRequestDto setUpMember() {
        memberRequestDto = new MemberRequestDto("heeverse12", "abcd1234!", "name", "email@naver.com");
        return memberRequestDto;
    }

    @Test
    @DisplayName("회원가입 성공한 Member 만이 로그인 가능함")
    public void signUpTest() throws Exception {
        mvc.perform(post("/member")
                        .content(objectMapper.writeValueAsString(setUpMember()))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated());

    }


    @Test
    @WithAnonymousUser
    @DisplayName("로그인에 성공하면 HttpStatus 는 redirection 다")
    void when_login_success_should_return_OK() throws Exception {

        // given
        memberService.signup(memberRequestDto);

        // when
        LoginRequestDto loginRequestDto = new LoginRequestDto(memberRequestDto.getId(), memberRequestDto.getPassword());

        // then
        mvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(result -> {
                    String attributeName = "org.springframework.security.web.context.RequestAttributeSecurityContextRepository.SPRING_SECURITY_CONTEXT";
                    SecurityContext securityContext
                            = (SecurityContext) result.getRequest().getAttribute(attributeName);
                    Authentication authentication = securityContext.getAuthentication();

                    assertInstanceOf(UsernamePasswordAuthenticationToken.class, authentication);
                    assertEquals(memberRequestDto.getId(), authentication.getPrincipal());
                })
                .andDo(print())
                .andExpect(status().is3xxRedirection());


    }


    @Test
    @WithAnonymousUser
    @DisplayName("로그인에 실패하면 Http Status 는 401 이다")
    void when_login_fail_should_return_401() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto(memberRequestDto.getId(), memberRequestDto.getPassword());

        mvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("익명의 USER 의 /member 요청은 인증,인가 permitAll 이므로 정상적으로 수행되어야 한다")
    @WithAnonymousUser
    void memeber_permitAll() throws Exception {

        mvc.perform(post("/member")
                        .content(objectMapper.writeValueAsString(memberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }




}

