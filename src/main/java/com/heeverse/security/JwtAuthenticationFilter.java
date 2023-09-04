package com.heeverse.security;

import com.heeverse.config.VaultOperationService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static com.heeverse.common.Constants.VAULT_PATH;
import static com.heeverse.common.Constants.VAULT_URL_SECRETES;

/**
 * @author jeongheekim
 * @date 2023/08/02
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final VaultOperationService vaultOperationService;


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        UrlProps urlProps = vaultOperationService.getProps(VAULT_PATH, VAULT_URL_SECRETES, UrlProps.class);
        String[] urlArr = urlProps.url().split(",");
        return Arrays.stream(urlArr).anyMatch(path::startsWith);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = jwtTokenProvider.parsing(request.getHeader(HttpHeaders.AUTHORIZATION));
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    private record UrlProps(String url) {
        UrlProps {
            Assert.notNull(url, "url must not null");
        }
    }
}
