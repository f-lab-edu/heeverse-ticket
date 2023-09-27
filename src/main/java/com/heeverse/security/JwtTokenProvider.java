package com.heeverse.security;

import com.heeverse.common.DateAdapter;
import com.heeverse.member.domain.entity.Member;
import com.heeverse.member.service.MemberService;
import com.heeverse.security.exception.JwtParsingException;
import com.heeverse.security.exception.VaultTokenNotExistException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultOperations;
import org.springframework.vault.support.VaultResponse;

import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.heeverse.common.Constants.*;
import static com.heeverse.security.ClaimConstants.*;

/**
 * @author jeongheekim
 * @date 2023/08/01
 */
@Slf4j
@Component
public class JwtTokenProvider implements InitializingBean {

    private final Duration TOKEN_DURATION_TIME = Duration.ofHours(1);
    private final MemberService memberService;
    private final VaultOperations vaultOperations;
    private Key key;

    public JwtTokenProvider(MemberService memberService,
                            VaultOperations vaultOperations) {
        this.memberService = memberService;
        this.vaultOperations = vaultOperations;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(getJwtKey()));
    }

    public String generateToken(String id, Authentication authentication) {
        Assert.notNull(authentication, "authentication은 not null입니다.");
        String auth = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        return Jwts.builder()
                .setClaims(claimToMap(id, auth))
                .setIssuedAt(new Date())
                .setExpiration(new DateAdapter(LocalDateTime.now().plus(TOKEN_DURATION_TIME)).toDate())
                .signWith(key)
                .compact();
    }

    private Map<String, String> claimToMap(String id, String auth) {
        Map<String, String> claimMap = new HashMap<>();
        claimMap.put(ID, id);
        claimMap.put(AUTH, auth);
        return claimMap;
    }

    public Authentication parsing(String headerAuth) {
        Claims claims = null;
        Collection<? extends GrantedAuthority> authorities = null;

        if (ObjectUtils.isEmpty(headerAuth)) {
            throw new IllegalArgumentException();
        }
        headerAuth = headerAuth.replaceAll(TOKEN_TYPE, "").trim();

        try {
            claims = createJwtClaims(headerAuth);
            authorities = Arrays.stream(claims.get(AUTH).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .toList();

        } catch (Exception e) {
            log.error("[JwtParsingException]{} : {}", e.getCause(), e.getMessage());
            throw new JwtParsingException(e.getMessage(), e);
        }

        return new UsernamePasswordAuthenticationToken(validateMember(claims), null, authorities);
    }

    private Claims createJwtClaims(String headerAuth) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(headerAuth)
                .getBody();
    }

    private String getJwtKey() {
        VaultKeyValueOperations keyValueOperations = vaultOperations.opsForKeyValue(VAULT_PATH,
                VaultKeyValueOperationsSupport.KeyValueBackend.KV_1);

        VaultResponse read = keyValueOperations.get(VAULT_AUTH_SECRETS);
        Assert.notNull(read, "vault read value must be null!");
        return Optional.ofNullable((String) read.getRequiredData().get(TOKEN_NAME))
                .orElseThrow(VaultTokenNotExistException::new);
    }

    private Member validateMember(Claims claims) {
        return memberService.findMember(claims.get(ID).toString())
                .orElseThrow(() -> new AuthenticationServiceException("존재하지 않는 멤버입니다."));
    }

}
