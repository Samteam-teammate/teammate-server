package com.samteam.teammate.domain.member.dto;

import com.samteam.teammate.domain.member.entity.Member;

import lombok.Builder;

@Builder
public record MemberRegisterResponse(
	// 로그인 완료 시 사용 되는 응답 형식
	Long id
) {
	public static MemberRegisterResponse from(Member member) {
		return MemberRegisterResponse.builder()
			.id(member.getId())
			.build();
	}
}
