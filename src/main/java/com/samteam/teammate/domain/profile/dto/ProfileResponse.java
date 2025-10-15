package com.samteam.teammate.domain.profile.dto;

import com.samteam.teammate.domain.profile.entity.Profile;
import com.samteam.teammate.global.enums.FieldType;
import com.samteam.teammate.global.enums.Major;

import lombok.Builder;

@Builder
public record ProfileResponse(
	Long id,
	String name,
	Major major,
	Integer grade,
	FieldType applicationField,
	String contactInfo,
	String additionalInfo
) {
	public static ProfileResponse from(Profile profile) {
		return ProfileResponse.builder()
			.id(profile.getId())
			.name(profile.getName())
			.major(profile.getMajor())
			.grade(profile.getGrade())
			.applicationField(profile.getApplicationField())
			.contactInfo(profile.getContactInfo())
			.additionalInfo(profile.getAdditionalInfo())
			.build();
	}
}
