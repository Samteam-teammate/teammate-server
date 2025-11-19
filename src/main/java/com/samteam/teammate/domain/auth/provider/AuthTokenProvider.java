package com.samteam.teammate.domain.auth.provider;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
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

    private String buildToken(String subject, long expireSeconds, Map<String, Object> claims) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + 1000L * expireSeconds);

        return Jwts.builder()
            .subject(subject)
            .issuedAt(issuedAt)
            .expiration(expiration)
            .claims(claims)
            .signWith(secretKey())
            .compact();
    }

    public String createAccessToken(Long id, Map<String, Object> claims) {
        return buildToken(String.valueOf(id), jwtAccessExpireSeconds, claims);
    }

    public String createRefreshToken(Long id, Map<String, Object> claims) {
        return buildToken(String.valueOf(id), jwtRefreshExpireSeconds, claims);
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parse(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
            .verifyWith(secretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
