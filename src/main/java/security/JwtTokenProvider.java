package security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.springframework.stereotype.Component;

/**
 * @author jeongheekim
 * @date 2023/08/01
 */
@Component
public class JwtTokenProvider {

    private final long VALID_SECOND = 1000L * 60 * 60;
    public static final String JWT_KEY = "d92427fa5d55bce3a2be379b4258bb9bb17d509c885c4241aa44a858f7754145";

    public String generateToken(String id) {
        return Jwts.builder().claim("id", id)
            .setIssuedAt(new Date())
            .setExpiration(new Date(new Date().getTime() + VALID_SECOND))
            .signWith(getSecretKey())
            .compact();
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8));
    }

}
