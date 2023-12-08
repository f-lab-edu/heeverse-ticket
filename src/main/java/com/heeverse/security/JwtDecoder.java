package com.heeverse.security;

import com.heeverse.common.DateAdapter;
import com.heeverse.config.VaultOperationService;
import com.heeverse.member.dto.AuthenticatedMember;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.heeverse.common.Constants.*;
import static com.heeverse.security.ClaimConstants.*;

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

    public Claims toClaims(String header) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(header)
                .getBody();
    }

    public String generateJwtToken(AuthenticatedMember member, Collection<? extends GrantedAuthority> authorities) {
        return Jwts.builder()
                .setClaims(claimToMap(member, authorities))
                .setIssuedAt(new Date())
                .setExpiration(new DateAdapter(LocalDateTime.now().plus(TOKEN_DURATION_TIME)).toDate())
                .signWith(key)
                .compact();
    }

    private void loadSigningKey() {
        Map<String, Object> props = vaultService.getProps(VAULT_PATH, VAULT_AUTH_SECRETS);
        String key = (String) props.get(TOKEN_NAME);
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(key));
    }

    private Map<String, String> claimToMap(AuthenticatedMember member, Collection<? extends GrantedAuthority> authorities) {
        Map<String, String> claimMap = new HashMap<>();
        claimMap.put(ID, member.getMemberId());
        claimMap.put(MEMBER_SEQ, Long.toString(member.getSeq()));
        claimMap.put(AUTH, convertToString(authorities));
        return claimMap;
    }

    private String convertToString(Collection<? extends GrantedAuthority> authorities) {
        return authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

}
