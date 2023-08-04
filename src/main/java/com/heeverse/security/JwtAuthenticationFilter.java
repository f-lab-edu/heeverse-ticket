package com.heeverse.security;

import com.heeverse.security.JwtTokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * @author jeongheekim
 * @date 2023/08/02
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    public static final String HEADER_NAME = "Authorization";
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {

        String jwtToken = jwtTokenProvider.parsing(request.getHeader(HEADER_NAME));
        if (!ObjectUtils.isEmpty(jwtToken) && jwtTokenProvider.validateToken(jwtToken)) {
            Authentication auth = jwtTokenProvider.getAuthentication(jwtToken);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}
