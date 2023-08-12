package com.heeverse.security;

import static com.heeverse.common.Constants.TOKEN_NAME;
import static com.heeverse.common.Constants.VAULT_PATH;
import static com.heeverse.common.Constants.VAULT_SECRETS;

import com.heeverse.common.DateAdapter;
import com.heeverse.member.domain.entity.Member;
import com.heeverse.member.service.MemberService;
import com.heeverse.security.exception.JwtParsingException;
import com.heeverse.security.exception.VaultTokenNotExistException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
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

    private final Duration TOKEN_DURATION_TIME = Duration.ofHours(1);

    private final MemberService memberService;
    private final VaultOperations vaultOperations;

    public JwtTokenProvider(MemberService memberService,
        VaultOperations vaultOperations) {
        this.memberService = memberService;
        this.vaultOperations = vaultOperations;
    }

    public String generateToken(String id, Authentication authentication) {
        return Jwts.builder().claim("id", id)
            .setIssuedAt(new Date())
            .setExpiration(new DateAdapter(LocalDateTime.now().plus(TOKEN_DURATION_TIME)).toDate())
            .signWith(getSecretKey())
            .compact();
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(getJwtKey().getBytes(StandardCharsets.UTF_8));
    }

    public Authentication getAuthentication(String jwtToken) {
        Member member = memberService.findMember(parsing(jwtToken))
            .orElseThrow(() -> new AuthenticationServiceException("존재하지 않는 멤버입니다."));
        return new UsernamePasswordAuthenticationToken(member.getId(), null);
    }

    public boolean validateToken(String jwtToken) {
        if (ObjectUtils.isEmpty(jwtToken)) {
            throw new IllegalArgumentException();
        }
        Jws<Claims> claims = Jwts.parserBuilder()
            .setSigningKey(getSecretKey())
            .build()
            .parseClaimsJws(jwtToken);
        return !claims.getBody().getExpiration().before(new Date());
    }

    public String parsing(String headerAuth) {
        if (ObjectUtils.isEmpty(headerAuth)) {
            throw new IllegalArgumentException();
        }
        try {
            return Jwts.parserBuilder()
                .requireAudience(getJwtKey())
                .build()
                .parse(headerAuth)
                .toString();
        } catch (Exception e) {
            throw new JwtParsingException();
        }
    }

    private String getJwtKey() {
        VaultKeyValueOperations keyValueOperations = vaultOperations.opsForKeyValue(VAULT_PATH,
            VaultKeyValueOperationsSupport.KeyValueBackend.KV_1);

        VaultResponse read = keyValueOperations.get(VAULT_SECRETS);
        Assert.notNull(read, "vault read value must be null!");
        return Optional.ofNullable((String) read.getRequiredData().get(TOKEN_NAME))
            .orElseThrow(VaultTokenNotExistException::new);
    }
}
