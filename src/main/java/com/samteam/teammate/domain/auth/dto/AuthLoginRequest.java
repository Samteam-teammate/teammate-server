package com.samteam.teammate.domain.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthLoginRequest(
    @NotNull
    Long studentId,

    @NotBlank
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String password
) {
}