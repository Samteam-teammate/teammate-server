package com.samteam.teammate.domain.auth.service;

import com.chuseok22.sejongportallogin.core.SejongMemberInfo;
import com.chuseok22.sejongportallogin.infrastructure.SejongPortalLoginService;
import com.samteam.teammate.domain.auth.dto.AuthLoginResponse;
import com.samteam.teammate.domain.auth.provider.AuthTokenProvider;
import com.samteam.teammate.domain.member.entity.Member;
import com.samteam.teammate.domain.member.repository.MemberRepository;
import com.samteam.teammate.global.exception.BusinessException;
import com.samteam.teammate.global.exception.docs.ErrorCode;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final SejongPortalLoginService portal;
    private final AuthTokenProvider authTokenProvider;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public AuthLoginResponse login(String studentId, String password, HttpServletResponse response) {
        SejongMemberInfo info = trySejongPortalLogin(studentId, password);

        String major = info.getMajor();
        int grade = Integer.parseInt(info.getGrade().replaceAll("[^0-9]", "")); // 학년만 추출

        // 사용자가 존재하지 않으면 예외 처리
        Member member = memberRepository.findByStudentId(Long.valueOf(studentId))
            .orElseThrow(() -> {
                // member, profile 생성을 위해 임시 토큰 발급
                // member 생성 요청시 sju login에 성공했음을 알기 위함
                issueTemporaryToken(response, Long.valueOf(studentId));
				return new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
            });

        // 사용자가 존재하면 jwt 발행
        issueToken(response, member.getId());

        // 응답 모델 구성
        return AuthLoginResponse.builder()
            .username(studentId)
            .major(major)
            .grade(grade)
            .build();
    }

    public void issueToken(HttpServletResponse response, Long id) {
        String accessToken = authTokenProvider.createAccessToken(id, "no");
        String refreshToken = authTokenProvider.createRefreshToken(id, "no");

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(604800) // 7일
            .sameSite("None")
            .build();

        // 쿠키 헤더를 수동으로 설정
        response.setHeader("Set-Cookie", refreshCookie.toString());
        response.setHeader("Authorization", "Bearer " + accessToken);
    }

    private void issueTemporaryToken(HttpServletResponse response, Long id) {
        // TODO: temp token 더 깔끔하게 발행
        String accessToken = authTokenProvider.createAccessToken(id, "yes");
        response.setHeader("Authorization", "Bearer " + accessToken);
    }

    private SejongMemberInfo trySejongPortalLogin(String studentId, String password) {
        try {
            // 1. 포털 인증 및 정보 획득 (크롤링 기반)
            // 주의: 이 라이브러리가 RuntimeException을 던질 가능성이 높습니다.
            return portal.getMemberAuthInfos(studentId, password);

        } catch (RuntimeException e) {
            // 2. 인증/연결 실패 처리 (로그인 실패, 포털 구조 변경, 타임아웃 등)

            if (e.getMessage().contains("Login fail")) {
                log.warn("Authentication failed for ID: {}", studentId);
                throw new BusinessException(ErrorCode.SJU_AUTH_FAILED);
            }

            // 크롤링 특성상 발생하는 통신 장애, 파싱 오류 등을 UPSTREAM_ERROR로 처리
            log.error("Upstream error (Sejong Portal) for ID: {}", studentId, e);
            throw new BusinessException(ErrorCode.UPSTREAM_ERROR);

        } catch (Exception e) {
            // 3. 예상치 못한 서버 내부 오류
            log.error("Uncaught exception during portal login: ", e);
            throw new BusinessException(ErrorCode.SERVER_ERROR);
        }
    }
}