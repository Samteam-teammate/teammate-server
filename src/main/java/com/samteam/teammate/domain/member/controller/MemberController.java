package com.samteam.teammate.domain.member.controller;

import com.samteam.teammate.domain.auth.service.AuthService;
import com.samteam.teammate.domain.member.dto.MemberRegisterResponse;
import com.samteam.teammate.domain.member.dto.MemberProfileResponse;
import com.samteam.teammate.domain.member.dto.MemberProfileUpdateRequest;
import com.samteam.teammate.domain.member.dto.MemberRegisterRequest;
import com.samteam.teammate.global.security.MemberPrincipal;
import com.samteam.teammate.domain.member.service.MemberService;
import com.samteam.teammate.global.util.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Tag(name = "Member API V1", description = "사용자 관련 API 엔드포인트 v1")
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    @Operation(summary = "사용자 등록 및 프로필 생성")
    @PostMapping
    public BaseResponse<MemberRegisterResponse> registerMember(
        @Valid @RequestBody MemberRegisterRequest request, HttpServletResponse response) {
        MemberRegisterResponse memberRegisterResponse = memberService.registerMember(request);
        authService.issueToken(response, memberRegisterResponse.id());

        return BaseResponse.success("회원가입에 성공했습니다", memberRegisterResponse);
    }

    @Operation(summary = "본인 프로필 조회")
    @GetMapping
    public BaseResponse<MemberProfileResponse> getProfile(
        @AuthenticationPrincipal MemberPrincipal principal
    ) {
        return BaseResponse.success("프로필 조회에 성공했습니다", memberService.getProfile(principal.id()));
    }

    @Operation(summary = "본인 프로필 수정")
    @PatchMapping
    public BaseResponse<MemberProfileResponse> updateProfile(
        @RequestBody MemberProfileUpdateRequest request,
        @AuthenticationPrincipal MemberPrincipal principal
    ) {
        var resp = memberService.updateProfile(principal.id(), request);
        return BaseResponse.success("프로필 수정에 성공했습니다", resp);
    }
}
