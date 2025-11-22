package com.samteam.teammate.domain.auth.provider;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

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

    public String getSubject(String token) {
        return Jwts.parser()
            .verifyWith(secretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public Claims getClaims(String token) {
        return Jwts.parser()
            .verifyWith(secretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }

    public Optional<String> getRefreshToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null || cookies.length == 0) {
            return Optional.empty();
        }

        return Arrays.stream(cookies)
            .filter(cookie -> "refreshToken".equals(cookie.getName()))
            .map(Cookie::getValue)
            .findFirst();
    }
}
