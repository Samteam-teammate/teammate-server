package com.samteam.teammate.domain.auth.controller;

import com.samteam.teammate.domain.auth.dto.AuthLoginRequest;
import com.samteam.teammate.domain.auth.dto.AuthLoginResponse;
import com.samteam.teammate.domain.auth.service.AuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API V1", description = "로그인 관련 API 엔드포인트 v1")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<AuthLoginResponse> login(
        @Valid @RequestBody AuthLoginRequest request,
        HttpServletResponse response) {

        AuthLoginResponse result = authService.login(request.studentId(), request.password(), response);

        return ResponseEntity.ok(result);
    }
}