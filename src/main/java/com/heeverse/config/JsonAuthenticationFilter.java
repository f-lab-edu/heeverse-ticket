package com.heeverse.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeverse.member.dto.LoginRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import security.JwtTokenProvider;

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


    public Authentication attemptAuthentication(
        HttpServletRequest request, HttpServletResponse response) {


            LoginRequestDto loginRequestDto = toLoginRequestDto(new ContentCachingRequestWrapper(request));
            return super.getAuthenticationManager()
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.id(), loginRequestDto.password()));
    }


    private LoginRequestDto toLoginRequestDto(HttpServletRequest request){
      
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
        String token = jwtTokenProvider.generateToken(principal);
        response.getWriter().write(token);
        response.getWriter().flush();

    }
}
