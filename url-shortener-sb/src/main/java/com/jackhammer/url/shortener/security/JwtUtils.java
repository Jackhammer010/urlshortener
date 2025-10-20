package com.jackhammer.url.shortener.security;

import com.jackhammer.url.shortener.service.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

public class JwtUtils {

    @Value("${jwt.secret.key}")
    private String secretKey;
    @Value("${jwt.expiration.minutes}")
    private long jwtExpirationMinutes;

    public String getJwtFromHeader(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer "))
            return bearerToken.substring(7);
        return null;
    }
    public String generateToken(UserDetailsImpl userDetails){
        String username = userDetails.getUsername();
        String roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        return Jwts.builder()
                .subject(username)
                .claim("roles", roles)
                .id(UUID.randomUUID().toString())
                .header().add("typ", "jwt")
                .and()
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 60 * 1000L * jwtExpirationMinutes))
                .signWith(getSigningKey())
                .compact();
    }
    public String getUsernameFromJwtToken(String token){
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload().getSubject();
    }
    public boolean validateToken (String token){
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build().parseSignedClaims(token);
            return true;
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    private SecretKey getSigningKey(){
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
    }
}
