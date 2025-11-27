package com.samteam.teammate.domain.profile.service;

import com.samteam.teammate.domain.member.entity.Member;
import com.samteam.teammate.domain.member.repository.MemberRepository;
import com.samteam.teammate.domain.profile.entity.Profile;
import com.samteam.teammate.domain.scrap.entity.ProfileScrap;
import com.samteam.teammate.domain.scrap.repository.ProfileScrapRepository;
import com.samteam.teammate.global.exception.BusinessException;
import com.samteam.teammate.global.exception.docs.ErrorCode;
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
    private final MemberRepository memberRepository;
    private final ProfileScrapRepository profileScrapRepository;

    public Page<ProfileResponse> getVisibleProfiles(
        Long memberId,
        Major major,
        TechType stack,
        FieldType field,
        Pageable pageable
    ) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Page<Profile> profiles = profileRepository
            .findAllByVisibilityTrueAndConditions(major, stack, field, pageable);

        return profiles.map(profile -> {
            // 로그인한 사용자가 해당 프로필을 스크랩했는지 조회
            var scrapOpt = profileScrapRepository.findByMemberAndProfile(member, profile);
            boolean scraped = scrapOpt.isPresent();
            Long scrapId = scrapOpt.map(ProfileScrap::getId).orElse(null);

            return ProfileResponse.of(profile, scraped, scrapId);
        });
    }
}
