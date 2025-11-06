package com.samteam.teammate.domain.member.dto;

import com.samteam.teammate.domain.member.entity.Member;

import lombok.Builder;

@Builder
public record MemberLoginResponse(
	// 로그인 완료 시 사용 되는 응답 형식
	Long id
) {
	public static MemberLoginResponse from(Member member) {
		return MemberLoginResponse.builder()
			.id(member.getId())
			.build();
	}
}
