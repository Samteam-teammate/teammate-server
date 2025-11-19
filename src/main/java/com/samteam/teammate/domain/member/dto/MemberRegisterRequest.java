package com.samteam.teammate.domain.member.dto;

import java.util.List;

import com.samteam.teammate.domain.member.entity.Member;
import com.samteam.teammate.domain.profile.entity.Profile;
import com.samteam.teammate.global.enums.FieldType;
import com.samteam.teammate.global.enums.Major;
import com.samteam.teammate.global.enums.TechType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MemberRegisterRequest(
	@NotNull Long studentId,
	@NotBlank String name,
	Major major,
	Integer grade,
	List<TechType> techStack,
	FieldType applicationField,
	String simpleInfo,
	String contactInfo,
	String additionalInfo,
	Boolean visibility
) {
	public static Profile to(MemberRegisterRequest request, Member member) {
		return Profile.builder()
			.member(member)
			.name(request.name())
			.major(request.major())
			.grade(request.grade())
			.teckStack(request.techStack())
			.applicationField(request.applicationField())
			.simpleInfo(request.simpleInfo())
			.contactInfo(request.contactInfo())
			.additionalInfo(request.additionalInfo())
			.visibility(request.visibility())
			.build();
	}
}
