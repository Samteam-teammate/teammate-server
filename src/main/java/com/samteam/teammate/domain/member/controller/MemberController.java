package com.samteam.teammate.domain.member.controller;

import com.samteam.teammate.domain.member.dto.MemberProfileResponse;
import com.samteam.teammate.domain.member.dto.MemberProfileUpdateRequest;
import com.samteam.teammate.global.security.MemberPrincipal;
import com.samteam.teammate.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<MemberProfileResponse> getProfile(
        @AuthenticationPrincipal MemberPrincipal principal
    ) {
        return ResponseEntity.ok(memberService.getProfile(principal.id()));
    }

    @Operation(summary = "본인 프로필 수정")
    @PatchMapping
    public ResponseEntity<MemberProfileResponse> updateProfile(
        @AuthenticationPrincipal MemberPrincipal principal,
        @RequestBody MemberProfileUpdateRequest request
    ) {
        var resp = memberService.updateProfile(principal.id(), request);
        return ResponseEntity.ok(resp);
    }
}
