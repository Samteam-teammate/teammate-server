package com.samteam.teammate.domain.member.dto;

import com.samteam.teammate.global.enums.FieldType;
import com.samteam.teammate.global.enums.Major;

public record MemberProfileUpdateRequest(
	String name,
	Major major,
	Integer grade,
	FieldType applicationField,
	String contactInfo,
	String additionalInfo,
	Boolean visibility
) {
}
