package com.samteam.teammate.domain.member.provider;

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

	public String genAccessToken(Long id) {

		SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

		Date issuedAt = new Date();
		Date expiration = new Date(issuedAt.getTime() + 1000L * jwtAccessExpireSeconds);

		return Jwts.builder()
			.subject(String.valueOf(id))
			.issuedAt(issuedAt)
			.expiration(expiration)
			.signWith(secretKey)
			.compact();
	}

	public String genRefreshToken(Long id) {

		SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

		Date issuedAt = new Date();
		Date expiration = new Date(issuedAt.getTime() + 1000L * jwtRefreshExpireSeconds);

		return Jwts.builder()
			.subject(String.valueOf(id))
			.issuedAt(issuedAt)
			.expiration(expiration)
			.signWith(secretKey)
			.compact();
	}

	public boolean isValidToken(String token) {
		try {
			SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());

			Jwts
				.parser()
				.verifyWith(secretKey)
				.build()
				.parse(token);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;

	}

	/*public Map<String, Object> verifyToken(String token) {
		try {
			SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());
			Claims claims = Jwts.parser()
				.verifyWith(secretKey)
				.build()
				.parseSignedClaims(token)
				.getPayload();
			return new HashMap<>(claims);
		} catch (JwtException e) {
			return null;
		}
	}*/

	public String getSubject(String token) {
		SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes());
		return Jwts.parser()
			.verifyWith(secretKey)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.getSubject();
	}
}
