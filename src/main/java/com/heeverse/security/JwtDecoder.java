package com.heeverse.security;

import com.heeverse.common.DateAdapter;
import com.heeverse.config.VaultOperationService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.heeverse.common.Constants.*;
import static com.heeverse.security.ClaimConstants.AUTH;
import static com.heeverse.security.ClaimConstants.ID;

@RequiredArgsConstructor
@Component
public class JwtDecoder {

    private final VaultOperationService vaultService;
    private Key key;
    public final static Duration TOKEN_DURATION_TIME = Duration.ofHours(1);

    @PostConstruct
    private void init() {
        loadSigningKey();
    }


    private void loadSigningKey() {
        Map<String, Object> props = vaultService.getProps(VAULT_PATH, VAULT_AUTH_SECRETS);
        String key = (String) props.get(TOKEN_NAME);
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    public Claims toClaims(String header) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(header)
                .getBody();
    }

    public String issueJwtToken(String principal, String auth) {
        return Jwts.builder()
                .setClaims(claimToMap(principal, auth))
                .setIssuedAt(new Date())
                .setExpiration(new DateAdapter(LocalDateTime.now().plus(TOKEN_DURATION_TIME)).toDate())
                .signWith(key)
                .compact();
    }

    private Map<String, String> claimToMap(String principal, String auth) {
        Map<String, String> claimMap = new HashMap<>();
        claimMap.put(ID, principal);
        claimMap.put(AUTH, auth);
        return claimMap;
    }

}
