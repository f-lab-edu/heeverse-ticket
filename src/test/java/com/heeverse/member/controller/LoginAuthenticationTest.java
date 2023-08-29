package com.heeverse.member.controller;

import com.heeverse.concert.service.ConcertService;
import com.heeverse.ControllerTestHelper;
import com.heeverse.member.service.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@ActiveProfiles("local")
@WebMvcTest(controllers = {ConcertService.class, MemberController.class})
@AutoConfigureMockMvc
class LoginAuthenticationTest {

    private final String LOGIN_URI = "/login";
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private MemberService memberService;
    @MockBean
    private ConcertService concertService;



    @BeforeEach
    public void setup() {
        mvc = ControllerTestHelper.getSecurityMockMvc(context);

    }


    @Test
    @WithMockUser(username = "heeverse12", password = "123")
    @DisplayName("Authentication 의 타입은 UsernamePasswordAuthenticationToken 이다")
    void when_success_login_authentication_should_return_usernamePwdAuthToken() throws Exception {

        mvc.perform(post(LOGIN_URI))
                .andExpect(authenticated().withAuthentication(auth ->
                        assertInstanceOf(UsernamePasswordAuthenticationToken.class, auth))
                );
    }

}

