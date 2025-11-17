package com.samteam.teammate.domain.profile.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.samteam.teammate.domain.profile.dto.ProfileResponse;
import com.samteam.teammate.domain.profile.repository.ProfileRepository;
import com.samteam.teammate.global.enums.FieldType;
import com.samteam.teammate.global.enums.Major;
import com.samteam.teammate.global.enums.TechType;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {

	private final ProfileRepository profileRepository;

	public Page<ProfileResponse> getVisibleProfiles(Major major, TechType stack, FieldType field, Pageable pageable) {
		return profileRepository.findAllByVisibilityTrueAndConditions(major, stack, field, pageable)
			.map(ProfileResponse::from);
	}
}
