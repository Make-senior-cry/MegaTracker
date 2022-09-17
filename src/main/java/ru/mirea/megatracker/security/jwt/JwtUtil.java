package ru.mirea.megatracker.security.jwt;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import ru.mirea.megatracker.security.UserDetailsImpl;

import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.token.secret}")
    private String jwtSecret;
    @Value("${jwt.token.expired}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            //TODO
        } catch (MalformedJwtException e) {
            //TODO
        } catch (ExpiredJwtException e) {
            //TODO
        } catch (UnsupportedJwtException e) {
            //TODO
        } catch (IllegalArgumentException e) {
            //TODO
        }

        return false;
    }

}
