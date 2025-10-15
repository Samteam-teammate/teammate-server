package com.samteam.teammate.domain.member.dto;

import jakarta.validation.constraints.NotBlank;

public record MemberLoginRequest(
	@NotBlank(message = "email을 입력해주세요.")
	String studentId,

	@NotBlank(message = "비밀번호를 입력해주세요.")
	String password
) {
}
