package com.samteam.teammate.domain.auth.dto;

public record AuthResponse(
	String msg,
	AuthResult result,
	String version
) {
}
