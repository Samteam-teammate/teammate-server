package com.samteam.teammate.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.samteam.teammate.domain.member.dto.MemberRegisterResponse;
import com.samteam.teammate.domain.member.dto.MemberProfileResponse;
import com.samteam.teammate.domain.member.dto.MemberProfileUpdateRequest;
import com.samteam.teammate.domain.member.dto.MemberRegisterRequest;
import com.samteam.teammate.domain.member.entity.Member;
import com.samteam.teammate.domain.member.repository.MemberRepository;
import com.samteam.teammate.domain.profile.entity.Profile;
import com.samteam.teammate.domain.profile.repository.ProfileRepository;
import com.samteam.teammate.global.enums.MemberRole;
import com.samteam.teammate.global.exception.BusinessException;
import com.samteam.teammate.global.exception.docs.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

	private final MemberRepository memberRepository;
	private final ProfileRepository profileRepository;

	@Transactional
	public MemberRegisterResponse registerMember(MemberRegisterRequest request) {
		if (memberRepository.existsByStudentId(request.studentId())) {
			throw new BusinessException(ErrorCode.MEMBER_ALREADY_EXIST);
		}

		Member member = Member.builder()
			.studentId(request.studentId())
			.role(MemberRole.ROLE_USER)
			.build();
		memberRepository.save(member);

		Profile profile = MemberRegisterRequest.to(request, member);
		profileRepository.save(profile);

		return MemberRegisterResponse.from(member);
	}

	@Transactional(readOnly = true)
	public MemberProfileResponse getProfile(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

		return MemberProfileResponse.from(profileRepository.findByMember(member));
	}

	@Transactional
	public MemberProfileResponse updateProfile(Long memberId, MemberProfileUpdateRequest request) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

		Profile profile = profileRepository.findByMember(member);
		profile.update(request);

		return MemberProfileResponse.from(profile);
 	}
}
