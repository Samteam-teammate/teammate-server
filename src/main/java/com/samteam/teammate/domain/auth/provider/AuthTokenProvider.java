package com.samteam.teammate.domain.auth.provider;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class AuthTokenProvider {

    @Value("${custom.jwt.secret-key}")
    private String secret;

    @Value("${custom.jwt.access-expire-seconds}")
    private int jwtAccessExpireSeconds;

    @Value("${custom.jwt.refresh-expire-seconds}")
    private int jwtRefreshExpireSeconds;

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    private String buildToken(String subject, long expireSeconds) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + 1000L * expireSeconds);

        return Jwts.builder()
            .subject(subject)
            .issuedAt(issuedAt)
            .expiration(expiration)
            .signWith(secretKey())
            .compact();
    }

    public String createAccessToken(Long id) {
        return buildToken(String.valueOf(id), jwtAccessExpireSeconds);
    }

    public String createRefreshToken(Long id) {
        return buildToken(String.valueOf(id), jwtRefreshExpireSeconds);
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parse(token);
            return true;
        } catch (Exception e) {
            // TODO: 로깅은 여기서 하거나 GlobalExceptionHandler에서
            return false;
        }
    }

    public String getSubject(String token) {
        return Jwts.parser()
            .verifyWith(secretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }
}
