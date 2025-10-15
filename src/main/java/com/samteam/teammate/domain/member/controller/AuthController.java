package com.samteam.teammate.domain.member.controller;

import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samteam.teammate.domain.member.dto.MemberLoginRequest;
import com.samteam.teammate.domain.member.dto.MemberLoginResponse;
import com.samteam.teammate.domain.member.service.AuthService;
import com.samteam.teammate.global.util.BasicResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Auth API V1", description = "로그인 및 사용자 인증 관련 API 엔드포인트 v1")
public class AuthController {

	private final AuthService authService;

	@Operation(summary = "로그인")
	@PostMapping("/login")
	public ResponseEntity<BasicResponse> login(
		@RequestBody @Validated MemberLoginRequest request,
		HttpServletResponse response
	) {
		MemberLoginResponse loginResponse = authService.login(Long.valueOf(request.studentId()), request.password());
		issueToken(response, loginResponse);

		BasicResponse basicResponse = BasicResponse.of("로그인 성공");

		return ResponseEntity.ok(basicResponse);
	}

	private void issueToken(HttpServletResponse response, MemberLoginResponse member) {
		String accessToken = authService.genAccessToken(member);
		String refreshToken = authService.genRefreshToken(member);

		ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
			.httpOnly(true)
			.secure(true)
			.path("/")
			.maxAge(604800) // 7일
			.sameSite("None")
			.build();

		// 쿠키 헤더를 수동으로 설정
		response.setHeader("Set-Cookie", refreshCookie.toString());
		response.setHeader("Authorization", "Bearer " + accessToken);
	}
}
