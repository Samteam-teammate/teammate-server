package com.samteam.teammate.domain.auth.dto;

import lombok.Builder;

@Builder
public record AuthRequest(
	String id,
	String pw,
	String method
) {
}
