package com.samteam.teammate.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
    @NotBlank
    Long studentId,

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password
) {
}