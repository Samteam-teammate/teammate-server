package com.samteam.teammate.domain.scrap.service;

import com.samteam.teammate.domain.member.entity.Member;
import com.samteam.teammate.domain.member.repository.MemberRepository;
import com.samteam.teammate.domain.profile.entity.Profile;
import com.samteam.teammate.domain.profile.repository.ProfileRepository;
import com.samteam.teammate.domain.scrap.dto.ScrapResponse;
import com.samteam.teammate.domain.scrap.dto.ScrapedProfileResponse;
import com.samteam.teammate.domain.scrap.entity.ProfileScrap;
import com.samteam.teammate.domain.scrap.repository.ProfileScrapRepository;
import com.samteam.teammate.global.exception.BusinessException;
import com.samteam.teammate.global.exception.docs.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileScrapService {

    private final ProfileScrapRepository scrapRepository;
    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;

    @Transactional
    public ScrapResponse scrapProfile(Long memberId, Long profileId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

        var already = scrapRepository.findByMemberAndProfile(member, profile);
        if (already.isPresent()) {
            return ScrapResponse.of(true);
        }

        ProfileScrap scrap = ProfileScrap.create(member, profile);
        scrapRepository.save(scrap);

        return ScrapResponse.of(true);
    }

    @Transactional
    public ScrapResponse cancelScrap(Long memberId, Long profileId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        Profile profile = profileRepository.findById(profileId)
            .orElseThrow(() -> new BusinessException(ErrorCode.PROFILE_NOT_FOUND));

        ProfileScrap scrap = scrapRepository.findByMemberAndProfile(member, profile)
            .orElseThrow(() -> new BusinessException(ErrorCode.SCRAP_NOT_FOUND));

        Long scrapId = scrap.getId();
        scrapRepository.delete(scrap);

        return ScrapResponse.of(false);
    }

    @Transactional(readOnly = true)
    public Page<ScrapedProfileResponse> getMyScraps(Long memberId, Pageable pageable) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        return scrapRepository.findByMember(member, pageable)
            .map(ScrapedProfileResponse::from);
    }

}