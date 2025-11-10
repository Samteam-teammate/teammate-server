package com.samteam.teammate.domain.auth.dto;

public record AuthResult(
	String authenticator,
	AuthBody body,
	String code,
	Boolean isAuth,
	Integer statusCode,
	String success
) {
}
