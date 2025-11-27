package com.samteam.teammate.domain.scrap.controller;

import com.samteam.teammate.domain.scrap.dto.ScrapResponse;
import com.samteam.teammate.domain.scrap.service.ProfileScrapService;
import com.samteam.teammate.global.security.MemberPrincipal;
import com.samteam.teammate.global.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileScrapController {

    private final ProfileScrapService profileScrapService;

    @PostMapping("/{profileId}/scrap")
    public BaseResponse<ScrapResponse> scrap(
        @PathVariable Long profileId,
        @AuthenticationPrincipal MemberPrincipal principal
    ) {
        Long memberId = Long.valueOf(principal.getUsername());
        ScrapResponse resp = profileScrapService.scrapProfile(memberId, profileId);
        return BaseResponse.success("스크랩에 성공했습니다", resp);
    }

    @DeleteMapping("/{profileId}/scrap")
    public BaseResponse<ScrapResponse> cancelScrap(
        @PathVariable Long profileId,
        @AuthenticationPrincipal MemberPrincipal principal
    ) {
        Long memberId = Long.valueOf(principal.getUsername());
        ScrapResponse resp = profileScrapService.cancelScrap(memberId, profileId);
        return BaseResponse.success("스크랩 취소에 성공했습니다", resp);
    }
}
