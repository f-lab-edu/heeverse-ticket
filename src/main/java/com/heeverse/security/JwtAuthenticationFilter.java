package com.heeverse.security;

import com.heeverse.config.SecurityConfig;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static com.heeverse.security.ClaimConstants.TOKEN_TYPE;

/**
 * @author jeongheekim
 * @date 2023/08/02
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final SecurityConfig.UrlProps urlProps;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        String[] urlArr = urlProps.url().split(",");
        return Arrays.stream(urlArr).anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication authentication = null;
        try {
            String token = request.getHeader(HttpHeaders.AUTHORIZATION).replaceAll(TOKEN_TYPE, "").trim();
            if(jwtProvider.validate(token)){
                authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (ExpiredJwtException e) {
            log.info("Token refreshed");
            response.setHeader(HttpHeaders.AUTHORIZATION, TOKEN_TYPE + StringUtils.SPACE + jwtProvider.generateToken(authentication));
        }

        filterChain.doFilter(request, response);
    }

}
