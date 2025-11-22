package com.samteam.teammate.domain.auth.controller;

import com.samteam.teammate.domain.auth.dto.AuthLoginRequest;
import com.samteam.teammate.domain.auth.dto.AuthLoginResponse;
import com.samteam.teammate.domain.auth.service.AuthService;
import com.samteam.teammate.global.util.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API V1", description = "로그인 관련 API 엔드포인트 v1")
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public BaseResponse<AuthLoginResponse> login(
        @Valid @RequestBody AuthLoginRequest request,
        HttpServletResponse response) {

        AuthLoginResponse result = authService.login(request.studentId(), request.password(), response);

        return BaseResponse.success("로그인에 성공했습니다", result);
    }

    @Operation(summary = "토큰 재발급")
    @GetMapping("/refresh")
    public BaseResponse<?> refresh(HttpServletRequest request, HttpServletResponse response) {
        authService.reissueToken(request, response);

        return BaseResponse.success("토큰 재발급에 성공했습니다");
    }

    @Operation(summary = "로그아웃", description = "프론트에서 헤더의 Access Token을 삭제해야함")
    @PostMapping("/logout")
    public BaseResponse<?> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);

        return BaseResponse.success("로그아웃에 성공했습니다");
    }
}