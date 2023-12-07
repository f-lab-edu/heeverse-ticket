package com.heeverse.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.heeverse.security.JsonAuthenticationFilter;
import com.heeverse.security.JwtAuthenticationFilter;
import com.heeverse.security.JwtProvider;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.util.Assert;

import static com.heeverse.common.Constants.VAULT_PATH;
import static com.heeverse.common.Constants.VAULT_URL_SECRETES;

/**
 * @author gutenlee
 * @since 2023/07/22
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final JwtProvider jwtProvider;
    private final VaultOperationService vaultOperationService;

    @Bean
    public UrlProps urlProps() {
        return vaultOperationService.getProps(VAULT_PATH, VAULT_URL_SECRETES, UrlProps.class);
    }


    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Profile("local")
    @Order(Ordered.HIGHEST_PRECEDENCE)
    SecurityFilterChain h2ConsoleSecurityFilterChain(HttpSecurity http) throws Exception {
        http.securityMatcher(PathRequest.toH2Console());
        http.csrf(AbstractHttpConfigurer::disable);
        http.headers((headers) -> headers.frameOptions(FrameOptionsConfig::sameOrigin));
        return http.build();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .securityContext(securityContext -> {
                    securityContext.securityContextRepository(
                            new HttpSessionSecurityContextRepository());
                })
                .addFilterAt(new JwtAuthenticationFilter(jwtProvider, urlProps()), BasicAuthenticationFilter.class)
                .addFilterAt(new JsonAuthenticationFilter(
                        authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),
                        jwtProvider, objectMapper), UsernamePasswordAuthenticationFilter.class)

                .authorizeHttpRequests((request) -> request
                        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
                        .requestMatchers(HttpMethod.GET, "/docs/index.html").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/member").permitAll()
                        .requestMatchers(HttpMethod.POST, "/concert").permitAll()
                        .requestMatchers(HttpMethod.GET, "/concert").permitAll()
                        .anyRequest().authenticated())
        ;

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig)
            throws Exception {
        return authConfig.getAuthenticationManager();
    }
    public record UrlProps(String url) {
        public UrlProps {
            Assert.notNull(url, "url must not null");
        }
    }

}
