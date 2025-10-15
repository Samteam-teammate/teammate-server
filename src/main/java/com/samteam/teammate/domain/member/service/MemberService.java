package com.samteam.teammate.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samteam.teammate.domain.member.dto.MemberProfileResponse;
import com.samteam.teammate.domain.member.dto.MemberProfileUpdateRequest;
import com.samteam.teammate.domain.member.entity.Member;
import com.samteam.teammate.domain.member.repository.MemberRepository;
import com.samteam.teammate.domain.profile.entity.Profile;
import com.samteam.teammate.domain.profile.repository.ProfileRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final ProfileRepository profileRepository;

	@Transactional(readOnly = true)
	public MemberProfileResponse getProfile(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다"));

		return MemberProfileResponse.from(profileRepository.findByMember(member));
	}

	@Transactional
	public MemberProfileResponse updateProfile(Long memberId, MemberProfileUpdateRequest request) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new RuntimeException("사용자가 존재하지 않습니다"));

		Profile profile = profileRepository.findByMember(member);
		profile.update(request);

		return MemberProfileResponse.from(profile);
 	}
}
