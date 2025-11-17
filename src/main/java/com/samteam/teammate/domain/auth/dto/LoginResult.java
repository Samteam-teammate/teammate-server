package com.samteam.teammate.domain.auth.dto;

import lombok.Builder;

@Builder
public record LoginResult(
    String accessToken,
    UserMini user
) {
}
