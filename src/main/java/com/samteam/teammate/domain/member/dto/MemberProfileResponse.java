package com.samteam.teammate.domain.member.dto;

import com.samteam.teammate.domain.profile.entity.Profile;
import com.samteam.teammate.global.enums.FieldType;
import com.samteam.teammate.global.enums.Major;

import lombok.Builder;

@Builder
public record MemberProfileResponse(
	Long studentId,
	String name,
	Major major,
	Integer grade,
	FieldType applicationField,
	String contactInfo,
	String additionalInfo,
	Boolean visibility
) {
	public static MemberProfileResponse from(Profile profile) {
		return MemberProfileResponse.builder()
			.studentId(profile.getMember().getStudentId())
			.name(profile.getName())
			.major(profile.getMajor())
			.grade(profile.getGrade())
			.applicationField(profile.getApplicationField())
			.contactInfo(profile.getContactInfo())
			.additionalInfo(profile.getAdditionalInfo())
			.visibility(profile.getVisibility())
			.build();
	}
}
