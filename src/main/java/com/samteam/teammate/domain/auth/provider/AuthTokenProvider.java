package com.samteam.teammate.domain.auth.provider;

import java.util.Date;
import java.util.Objects;

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

    private String buildToken(String subject, long expireSeconds, String isTemp) {
        Date issuedAt = new Date();
        Date expiration = new Date(issuedAt.getTime() + 1000L * expireSeconds);

        return Jwts.builder()
            .subject(subject)
            .issuedAt(issuedAt)
            .expiration(expiration)
            .claim("temp", isTemp)
            .signWith(secretKey())
            .compact();
    }

    public String createAccessToken(Long id, String isTemp) {
        return buildToken(String.valueOf(id), jwtAccessExpireSeconds, isTemp);
    }

    public String createRefreshToken(Long id, String isTemp) {
        return buildToken(String.valueOf(id), jwtRefreshExpireSeconds, isTemp);
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

    public boolean isTemporaryToken(String token) {
        // TODO : temp token 여부 좀 더 깔끔하게
        try {
            System.out.println("valid temp");
            String temp = Jwts.parser()
                .verifyWith(secretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("temp", String.class);

            return Objects.equals(temp, "yes");
        } catch (Exception e) {
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
