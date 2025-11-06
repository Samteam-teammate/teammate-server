package com.samteam.teammate.domain.member.dto;

import com.samteam.teammate.domain.profile.entity.Profile;
import com.samteam.teammate.global.enums.FieldType;
import com.samteam.teammate.global.enums.Major;
import com.samteam.teammate.global.enums.TechType;

import lombok.Builder;

@Builder
public record MemberProfileResponse(
	// 본인 프로필 조회 시 사용 되는 응답 형식
	Long studentId,
	String name,
	Major major,
	Integer grade,
	TechType techStack,
	FieldType applicationField,
	String contactInfo,
	String simpleInfo,
	String additionalInfo,
	Boolean visibility
) {
	public static MemberProfileResponse from(Profile profile) {
		return MemberProfileResponse.builder()
			.studentId(profile.getMember().getStudentId())
			.name(profile.getName())
			.major(profile.getMajor())
			.grade(profile.getGrade())
			.techStack(profile.getTeckStack())
			.applicationField(profile.getApplicationField())
			.contactInfo(profile.getContactInfo())
			.simpleInfo(profile.getSimpleInfo())
			.additionalInfo(profile.getAdditionalInfo())
			.visibility(profile.getVisibility())
			.build();
	}
}
