package com.heeverse.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeverse.ControllerTestHelper;
import com.heeverse.member.dto.LoginRequestDto;
import com.heeverse.member.dto.MemberRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author gutenlee
 * @since 2023/08/26
 */

@SpringBootTest
@Transactional
@ActiveProfiles(profiles = "local")
public class MemberIntegrationTest {

    private final String LOGIN_URI = "/login";
    private final String SIGN_UP_URI = "/member";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private MemberRequestDto successMemberRequestDto = new MemberRequestDto("heeverse12", "abcd1234!", "name", "email@naver.com");
    private MemberRequestDto failMemberRequestDto = new MemberRequestDto("heeverse12", "12", "name", "email@naver.com");

    @BeforeEach
    void setUp() {
        mockMvc = ControllerTestHelper.getSecurityMockMvc(context);
    }

    @Test
    @WithAnonymousUser
    @DisplayName("회원가입 성공 - 201")
    void 회원가입_성공() throws Exception {

        mockMvc.perform(post(SIGN_UP_URI)
                        .content(objectMapper.writeValueAsString(successMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }


    @Test
    @WithAnonymousUser
    @DisplayName("회원가입 데이터 기준 미달하면 실패한다 - 400")
    void 회원가입_실패() throws Exception {

        mockMvc.perform(post(SIGN_UP_URI)
                        .content(objectMapper.writeValueAsString(failMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName("이미 가입된 회원은 회원가입 요청 실패한다 - 409")
    void member_signUp_failed_exist_member() throws Exception {

        회원가입_성공();

        mockMvc.perform(post(SIGN_UP_URI)
                        .content(objectMapper.writeValueAsString(successMemberRequestDto))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }



    @Test
    @DisplayName("로그인 성공 - 200")
    void 로그인_성공() throws Exception {

        회원가입_성공();

        mockMvc.perform(post(LOGIN_URI)
                .content(objectMapper.writeValueAsString(
                        new LoginRequestDto(successMemberRequestDto.getUserName(), successMemberRequestDto.getPassword()))
                ));
    }


    @Test
    @WithAnonymousUser
    @DisplayName("회원 정보가 없으면 로그인 실패한다 - 400")
    void login_failed() throws Exception {

        mockMvc.perform(post(LOGIN_URI)
                .content(objectMapper.writeValueAsString(
                        new LoginRequestDto(failMemberRequestDto.getId(), failMemberRequestDto.getUserName()))
                ));
    }


}
