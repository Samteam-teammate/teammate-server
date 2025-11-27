package com.samteam.teammate.domain.scrap.controller;

import com.samteam.teammate.domain.scrap.dto.ScrapResponse;
import com.samteam.teammate.domain.scrap.dto.ScrapedProfileResponse;
import com.samteam.teammate.domain.scrap.service.ProfileScrapService;
import com.samteam.teammate.global.security.MemberPrincipal;
import com.samteam.teammate.global.util.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(name = "Profile Scrap API V1", description = "프로필 스크랩 관련 API v1")
public class ProfileScrapController {

    private final ProfileScrapService profileScrapService;

    @Operation(summary = "프로필 스크랩 등록")
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/profiles/{profileId}/scrap")
    public BaseResponse<ScrapResponse> scrap(
        @PathVariable Long profileId,
        @AuthenticationPrincipal MemberPrincipal principal
    ) {
        Long memberId = Long.valueOf(principal.getUsername());
        ScrapResponse resp = profileScrapService.scrapProfile(memberId, profileId);
        return BaseResponse.success("스크랩에 성공했습니다", resp);
    }

    @Operation(summary = "프로필 스크랩 취소")
    @PreAuthorize("hasRole('USER')")
    @DeleteMapping("/profiles/{profileId}/scrap")
    public BaseResponse<ScrapResponse> cancelScrap(
        @PathVariable Long profileId,
        @AuthenticationPrincipal MemberPrincipal principal
    ) {
        Long memberId = Long.valueOf(principal.getUsername());
        ScrapResponse resp = profileScrapService.cancelScrap(memberId, profileId);
        return BaseResponse.success("스크랩 취소에 성공했습니다", resp);
    }

    @Operation(summary = "스크랩한 프로필 목록 조회")
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/my/scraps")
    public BaseResponse<Page<ScrapedProfileResponse>> getMyScraps(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int size,
        @AuthenticationPrincipal MemberPrincipal principal
    ) {
        Long memberId = Long.valueOf(principal.getUsername());
        Pageable pageable = PageRequest.of(page, size);
        Page<ScrapedProfileResponse> result = profileScrapService.getMyScraps(memberId, pageable);
        return BaseResponse.success("스크랩한 프로필 목록 조회에 성공했습니다", result);
    }
}