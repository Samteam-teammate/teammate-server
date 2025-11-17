package com.samteam.teammate.domain.auth.dto;

import lombok.Builder;

@Builder
public record UserMini(
    String username, // 학번
    String major,    // 학과
    Integer grade    // 학년
) {
}