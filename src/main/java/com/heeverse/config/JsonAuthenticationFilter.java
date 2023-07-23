package com.heeverse.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeverse.member.dto.LoginDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * JSON 구조의 Login 데이터를 AuthenticationToken 으로 변환
 *
 * @author gutenlee
 * @since 2023/07/23
 */
public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonAuthenticationFilter(AuthenticationManager authenticationManager) {
        super();
        super.setAuthenticationManager(authenticationManager);
    }


    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) {

        try {

            LoginDto loginDto = toLoginDto(request);
            return super.getAuthenticationManager()
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.id(), loginDto.password()));

        } catch (IOException e) {
            throw new AuthenticationServiceException("로그인을 처리할 수 없습니다");
        }

    }

    private LoginDto toLoginDto(HttpServletRequest request) throws IOException {
        String body = request.getReader().lines().collect(Collectors.joining());
        return objectMapper.readValue(body, LoginDto.class);
    }
}
