package com.heeverse.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeverse.member.dto.LoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static com.heeverse.security.ClaimConstants.TOKEN_TYPE;

/**
 * JSON 구조의 Login 데이터를 AuthenticationToken 으로 변환
 *
 * @author gutenlee
 * @since 2023/07/23
 */
public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;

    public JsonAuthenticationFilter(
        AuthenticationManager authenticationManager,
        JwtProvider jwtProvider,
        ObjectMapper objectMapper
    ) {
        super.setAuthenticationManager(authenticationManager);
        this.jwtProvider = jwtProvider;
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
        HttpServletResponse response, FilterChain chain, Authentication authResult) {

        String principal = (String) authResult.getPrincipal();
        String token = jwtProvider.generateToken(principal, authResult);

        response.setHeader(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + " " + token);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
    }

}
