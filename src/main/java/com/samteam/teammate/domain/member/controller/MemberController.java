package com.samteam.teammate.domain.member.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.samteam.teammate.domain.member.dto.MemberProfileResponse;
import com.samteam.teammate.domain.member.dto.MemberProfileUpdateRequest;
import com.samteam.teammate.domain.member.provider.AuthTokenProvider;
import com.samteam.teammate.domain.member.service.MemberService;

import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "Member API V1", description = "사용자 관련 API 엔드포인트 v1")
public class MemberController {
	// 본인 프로필 수정/조회만
	private final MemberService memberService;

	@Operation(summary = "본인 프로필 조회")
	@GetMapping()
	public ResponseEntity<MemberProfileResponse> getProfile(HttpServletRequest request) {
		return ResponseEntity.ok(memberService.getProfile(getCurrentMember(request)));
	}

	@Operation(summary = "본인 프로필 수정")
	@PatchMapping()
	public ResponseEntity<MemberProfileResponse> updateProfile(
		HttpServletRequest request, // 임시
		@RequestBody MemberProfileUpdateRequest memberProfileUpdateRequest) {

		MemberProfileResponse response = memberService.updateProfile(getCurrentMember(request), memberProfileUpdateRequest);

		return ResponseEntity.ok(response);
	}

	// 임시 메서드
	private final AuthTokenProvider provider;
	private Long getCurrentMember(HttpServletRequest request) {
		String bearerToken = request.getHeader("Authorization");

		if (!StringUtils.hasText(bearerToken) || !bearerToken.startsWith("Bearer ")) {
			throw new RuntimeException("토큰이 존재하지 않음");
		}

		String token = bearerToken.substring(7);

		if (!provider.isValidToken(token)) {
			throw new  RuntimeException("유효하지 않은 토큰입니다.");
		}

		long memberId;
		try {
			memberId = Long.parseLong(provider.getSubject(token));

		} catch (JwtException | IllegalArgumentException e) {
			throw new RuntimeException("토큰에서 사용자 정보를 파싱할 수 없습니다.");
		}

		return memberId;
	}
}
