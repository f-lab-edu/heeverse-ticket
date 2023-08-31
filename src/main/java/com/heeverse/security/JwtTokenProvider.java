package com.heeverse.security;

import com.heeverse.common.DateAdapter;
import com.heeverse.member.domain.entity.Member;
import com.heeverse.member.service.MemberService;
import com.heeverse.security.exception.JwtParsingException;
import com.heeverse.security.exception.TokenExpiredException;
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
        String auth = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        Map<String, String> claimMap = new HashMap<>();
        claimMap.put(CLIAM_ID_KEY, id);
        claimMap.put(CLAIM_AUTH_KEY, auth);

        return Jwts.builder()
                .setClaims(claimMap)
                .setIssuedAt(new Date())
                .setExpiration(new DateAdapter(LocalDateTime.now().plus(TOKEN_DURATION_TIME)).toDate())
                .signWith(key)
                .compact();
    }

    public Authentication parsing(String headerAuth) {
        if (ObjectUtils.isEmpty(headerAuth)) {
            throw new IllegalArgumentException();
        }

        try {
            headerAuth = headerAuth.replaceAll(TOKEN_TYPE, "").trim();
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(headerAuth)
                    .getBody();

            if (!claims.getExpiration().before(new Date())) {
                throw new TokenExpiredException();
            }

            Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(CLAIM_AUTH_KEY).toString().split(","))
                    .map(SimpleGrantedAuthority::new)
                    .toList();

            Member member = memberService.findMember(claims.get(CLIAM_ID_KEY).toString())
                    .orElseThrow(() -> new AuthenticationServiceException("존재하지 않는 멤버입니다."));
            return new UsernamePasswordAuthenticationToken(member, null,authorities);

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
