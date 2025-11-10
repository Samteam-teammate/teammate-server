package com.samteam.teammate.domain.member.dto;

import java.util.List;

import com.samteam.teammate.global.enums.FieldType;
import com.samteam.teammate.global.enums.Major;
import com.samteam.teammate.global.enums.TechType;

public record MemberProfileUpdateRequest(
	// 본인 프로필 수정 시 사용 되는 요청 형식
	String name,
	Major major,
	Integer grade,
	List<TechType> techStack,
	FieldType applicationField,
	String simpleInfo,
	String contactInfo,
	String additionalInfo,
	Boolean visibility
) {
}
