package com.samteam.teammate.domain.profile.dto;

import java.util.List;

import com.samteam.teammate.domain.profile.entity.Profile;
import com.samteam.teammate.global.enums.FieldType;
import com.samteam.teammate.global.enums.Major;
import com.samteam.teammate.global.enums.TechType;

import lombok.Builder;

@Builder
public record ProfileResponse(
	Long id,
	String name,
	Major major,
	Integer grade,
	List<TechType> techStack,
	FieldType applicationField,
	String simpleInfo,
	String contactInfo,
	String additionalInfo,
    Boolean scraped
) {
	public static ProfileResponse of(Profile profile, boolean scraped) {
		return ProfileResponse.builder()
			.id(profile.getId())
			.name(profile.getName())
			.major(profile.getMajor())
			.grade(profile.getGrade())
			.techStack(profile.getTeckStack())
			.applicationField(profile.getApplicationField())
			.contactInfo(profile.getContactInfo())
			.simpleInfo(profile.getSimpleInfo())
			.additionalInfo(profile.getAdditionalInfo())
            .scraped(scraped)
			.build();
	}
}
