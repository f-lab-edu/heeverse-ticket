package com.heeverse.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeverse.config.LoginAuthenticationProvider;
import com.heeverse.member.dto.LoginRequestDto;
import com.heeverse.member.dto.MemberRequestDto;
import com.heeverse.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
class LoginAuthenticationTest {

    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @TestConfiguration
    static class TestConfig{

        @Bean
        MemberService memberService() {
            return new StubMemberService();
        }
        @Bean
        LoginAuthenticationProvider loginAuthenticationProvider(){
            return new LoginAuthenticationProvider(memberService(), new BCryptPasswordEncoder());
        }
    }

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }


    @Test
    @WithAnonymousUser
    @DisplayName("로그인에 성공하면 HttpStatus 는 OK 다")
    void when_login_success_should_return_OK() throws Exception {

        LoginRequestDto loginRequestDto = new LoginRequestDto("okUser", "123");

        mvc.perform(post("/login")
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithAnonymousUser
    @DisplayName("로그인에 실패하면 Http Status 는 401 이다")
    void when_login_fail_should_return_401() throws Exception {
        LoginRequestDto loginRequestDto = new LoginRequestDto("heeverse", "1026");

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
                        .content(objectMapper.writeValueAsString(new MemberRequestDto("a","bb","cc","dd@gmail.com")))
                        .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated());
    }




}

