package com.heeverse.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeverse.member.dto.LoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
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
    private final JwtTokenProvider jwtTokenProvider;

    public JsonAuthenticationFilter(
        AuthenticationManager authenticationManager,
        JwtTokenProvider jwtTokenProvider,
        ObjectMapper objectMapper
    ) {
        super.setAuthenticationManager(authenticationManager);
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request, HttpServletResponse response) {

        LoginRequestDto loginRequestDto = toLoginDto(new ContentCachingRequestWrapper(request));
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
            loginRequestDto.id(), loginRequestDto.password());
        setDetails(request, token);
        return super.getAuthenticationManager()
            .authenticate(token);
    }


    private LoginRequestDto toLoginDto(HttpServletRequest request) {
        String body;
        try {
            body = request.getReader()
                .lines()
                .collect(Collectors.joining());
            return objectMapper.readValue(body, LoginRequestDto.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException("로그인을 처리할 수 없습니다");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {

        String principal = (String) authResult.getPrincipal();
        String token = jwtTokenProvider.generateToken(principal, authResult);

        response.setHeader("Authorization", "Bearer " + token);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
    }

}
