package polimi.aui.sentimentaigroup6b.security;

import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import polimi.aui.sentimentaigroup6b.entities.User;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * This class contains the methods necessary to manage JWT
 */
@Component("JwtUtil")
@SuppressWarnings("deprecation")
public class JwtUtil {


    private final String secret_key = "mysecretkeyaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    private final long accessTokenValidity = 1440;

    private final JwtParser jwtParser;

    private final String TOKEN_HEADER = "Authorization";
    private final String TOKEN_PREFIX = "Bearer";

    public JwtUtil(){
        this.jwtParser = Jwts.parser().setSigningKey(secret_key).build();
    }

    public String createToken(User user) {
        Date tokenCreateTime = new Date();
        Date tokenValidity = new Date(tokenCreateTime.getTime() + TimeUnit.MINUTES.toMillis(accessTokenValidity));

        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(tokenCreateTime)
                .setExpiration(tokenValidity)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }

    private Claims parseJwtClaims(String token) {
        return jwtParser.parseClaimsJws(token).getBody();
    }

    public Claims resolveClaims(HttpServletRequest req) {
        try {
            String token = resolveToken(req);
            if (token != null) {
                return parseJwtClaims(token);
            }
            return null;
        } catch (ExpiredJwtException ex) {
            req.setAttribute("expired", ex.getMessage());
            throw ex;
        } catch (Exception ex) {
            req.setAttribute("invalid", ex.getMessage());
            throw ex;
        }
    }

    public String resolveToken(HttpServletRequest request) {

        String bearerToken = request.getHeader(TOKEN_HEADER);
        if (bearerToken != null && bearerToken.startsWith(TOKEN_PREFIX)) {
            return bearerToken.substring(TOKEN_PREFIX.length()).trim();
        }
        return null;
    }

    public boolean validateClaims(Claims claims) throws AuthenticationException {
        return claims.getExpiration().after(new Date());
    }

}
