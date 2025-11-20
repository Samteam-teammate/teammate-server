package com.samteam.teammate.domain.profile.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.samteam.teammate.domain.profile.dto.ProfileResponse;
import com.samteam.teammate.domain.profile.service.ProfileService;
import com.samteam.teammate.global.enums.FieldType;
import com.samteam.teammate.global.enums.Major;
import com.samteam.teammate.global.enums.TechType;
import com.samteam.teammate.global.util.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@Tag(name = "Profile API V1", description = "프로필 관련 API 엔드포인트 v1")
public class ProfileController {

	private final ProfileService profileService;

	@Operation(summary = "전체 프로필 조회")
	@PreAuthorize("hasRole('USER')")
	@GetMapping()
	public BaseResponse<Page<ProfileResponse>> getAllVisibleProfiles(
		@RequestParam(value = "major", required = false) Major major,
		@RequestParam(value = "stack", required = false) TechType stack,
		@RequestParam(value = "field", required = false) FieldType field,
		@RequestParam(value = "page", defaultValue = "0") int page,
		@RequestParam(value = "size", defaultValue = "20") int size) {

		Pageable pageable = PageRequest.of(page, size);

		Page<ProfileResponse> profiles= profileService.getVisibleProfiles(major, stack, field, pageable);

		return BaseResponse.success("프로필 목록 조회에 성공했습니다", profiles);
	}
}
