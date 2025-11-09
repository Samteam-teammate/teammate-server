package com.samteam.teammate.domain.member.controller;

import com.samteam.teammate.domain.member.dto.MemberProfileResponse;
import com.samteam.teammate.domain.member.dto.MemberProfileUpdateRequest;
import com.samteam.teammate.global.security.MemberPrincipal;
import com.samteam.teammate.domain.member.service.MemberService;
import com.samteam.teammate.global.util.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "Member API V1", description = "사용자 관련 API 엔드포인트 v1")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "본인 프로필 조회")
    @GetMapping
    public BaseResponse<MemberProfileResponse> getProfile(
        @AuthenticationPrincipal MemberPrincipal principal
    ) {
        return BaseResponse.success("프로필 조회에 성공했습니다.", memberService.getProfile(principal.id()));
    }

    @Operation(summary = "본인 프로필 수정")
    @PatchMapping
    public BaseResponse<MemberProfileResponse> updateProfile(
        @AuthenticationPrincipal MemberPrincipal principal,
        @RequestBody MemberProfileUpdateRequest request
    ) {
        var resp = memberService.updateProfile(principal.id(), request);
        return BaseResponse.success("프로필 수정에 성공했습니다.", resp);
    }
}
