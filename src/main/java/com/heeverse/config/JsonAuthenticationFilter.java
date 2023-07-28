package com.heeverse.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeverse.member.dto.LoginRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * JSON 구조의 Login 데이터를 AuthenticationToken 으로 변환
 *
 * @author gutenlee
 * @since 2023/07/23
 */
public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;

    public JsonAuthenticationFilter(
            AuthenticationManager authenticationManager,
            ObjectMapper objectMapper
    ) {
        super.setAuthenticationManager(authenticationManager);
        this.objectMapper = objectMapper;
    }


    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) {

            request = new ContentCachingRequestWrapper(request);
            LoginRequestDto loginRequestDto = toLoginDto(request);

            return super.getAuthenticationManager()
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.id(), loginRequestDto.password()));
    }


    private LoginRequestDto toLoginDto(HttpServletRequest request){
        String body;
        try {
            body = request.getReader()
                    .lines()
                    .collect(Collectors.joining());
            return objectMapper.readValue(body, LoginRequestDto.class);
        } catch (IOException e){
            throw new AuthenticationServiceException("로그인을 처리할 수 없습니다");
        }
    }
}
