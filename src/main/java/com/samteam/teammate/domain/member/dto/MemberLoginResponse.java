package com.samteam.teammate.domain.member.dto;

import com.samteam.teammate.domain.member.entity.Member;

import lombok.Builder;

@Builder
public record MemberLoginResponse(
	Long id
) {
	public static MemberLoginResponse from(Member member) {
		return MemberLoginResponse.builder()
			.id(member.getId())
			.build();
	}
}
