package com.samteam.teammate.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samteam.teammate.domain.member.dto.ProfileUpdateRequest;
import com.samteam.teammate.domain.member.entity.Member;
import com.samteam.teammate.domain.member.repository.MemberRepository;
import com.samteam.teammate.domain.profile.dto.ProfileResponse;
import com.samteam.teammate.domain.profile.entity.Profile;
import com.samteam.teammate.domain.profile.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final ProfileRepository profileRepository;

	@Transactional
	public ProfileResponse updateProfile(Long memberId, ProfileUpdateRequest request) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다"));

		Profile profile = profileRepository.findByMember(member);
		profile.update(request);

		return ProfileResponse.from(profile);
 	}
}
