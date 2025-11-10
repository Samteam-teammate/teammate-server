package com.samteam.teammate.domain.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
	// 로그인 요청시 사용 되는 요청 형식
	// 학번
	@NotBlank(message = "학번을 입력해주세요.")
	String studentId,

	// 포털 비밀 번호
	@NotBlank(message = "비밀번호를 입력해주세요.")
	String password
) {
}
