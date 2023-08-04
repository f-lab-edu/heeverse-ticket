package com.heeverse.security;

import com.heeverse.member.domain.entity.Member;
import com.heeverse.member.service.MemberService;
import com.heeverse.security.exception.JwtParsingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.util.Date;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.support.VaultResponse;

/**
 * @author jeongheekim
 * @date 2023/08/01
 */
@Component
public class JwtTokenProvider {

    private final Duration TOKEN_DURATION_TIME = Duration.ofMillis(3600000);
    private final String VAULT_PATH = "heeverse";
    private final String VAULT_SECRETS = "Authentication";
    private final String TOKEN_NAME = "JWT_KEY";
    private final MemberService memberService;
    private final VaultOperations vaultOperations;

    public JwtTokenProvider(MemberService memberService,
        VaultOperations vaultOperations) {
        this.memberService = memberService;
        this.vaultOperations = vaultOperations;
    }

    private String getJwtKey() {
        VaultKeyValueOperations keyValueOperations = vaultOperations.opsForKeyValue(VAULT_PATH,
            VaultKeyValueOperationsSupport.KeyValueBackend.KV_1);

        VaultResponse read = keyValueOperations.get(VAULT_SECRETS);
        assert read != null;
        return (String) read.getRequiredData().get(TOKEN_NAME);
    }

    public String generateToken(String id) {
        return Jwts.builder().claim("id", id)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + TOKEN_DURATION_TIME.toMillis()))
            .signWith(getSecretKey())
            .compact();
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(getJwtKey().getBytes(StandardCharsets.UTF_8));
    }

    public boolean validateToken(String jwtToken) {
        Jws<Claims> claims = Jwts.parserBuilder()
            .setSigningKey(getSecretKey())
            .build()
            .parseClaimsJws(jwtToken);
        return !claims.getBody().getExpiration().before(new Date());
    }

    public Authentication getAuthentication(String jwtToken) {
        Member member = memberService.findMember(parsing(jwtToken))
            .orElseThrow(() -> new AuthenticationServiceException("존재하지 않는 멤버입니다."));
        return new UsernamePasswordAuthenticationToken(member.getId(), null);
    }

    public String parsing(String headerAuth) {
        String id = null;
        if (!StringUtils.hasText(headerAuth)) {
            throw new IllegalArgumentException();
        }
        try {
            id = Jwts.parserBuilder()
                .requireAudience(getJwtKey())
                .build()
                .parse(headerAuth)
                .toString();
        } catch (JwtParsingException e) {
            e.printStackTrace();
        }
        return id;
    }
}
