package com.samteam.teammate.domain.auth.service;

import com.chuseok22.sejongportallogin.core.SejongMemberInfo;
import com.chuseok22.sejongportallogin.infrastructure.SejongPortalLoginService;
import com.samteam.teammate.domain.auth.dto.LoginResult;
import com.samteam.teammate.domain.auth.dto.UserMini;
import com.samteam.teammate.domain.member.provider.AuthTokenProvider;
import com.samteam.teammate.global.exception.BusinessException;
import com.samteam.teammate.global.exception.docs.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@RequiredArgsConstructor
public class PortalAuthService {

    private static final Logger log = LoggerFactory.getLogger(PortalAuthService.class);

    // 이 클래스들은 Bean으로 등록되어 있어야 합니다.
    private final SejongPortalLoginService portal;
    private final AuthTokenProvider jwt;

    public LoginResult login(Long id, String pw) {
        SejongMemberInfo info;

        try {
            // 1. 포털 인증 및 정보 획득 (크롤링 기반)
            // 주의: 이 라이브러리가 RuntimeException을 던질 가능성이 높습니다.
            info = portal.getMemberAuthInfos(id.toString(), pw);

        } catch (RuntimeException e) {
            // 2. 인증/연결 실패 처리 (로그인 실패, 포털 구조 변경, 타임아웃 등)
            // 로그 마스킹 규칙을 지키기 위해 id/pw는 직접 로그에 찍지 않습니다.

            // 포털 라이브러리가 BadCredentialsException을 던지지 않아 메시지 기반으로 분리 (약한 연결)
            if (e.getMessage().contains("Login fail")) {
                log.warn("Authentication failed for ID: {}", id);
                throw new BusinessException(ErrorCode.AUTH_FAILED);
            }

            // 크롤링 특성상 발생하는 통신 장애, 파싱 오류 등을 UPSTREAM_ERROR로 처리
            log.error("Upstream error (Sejong Portal) for ID: {}", id, e);
            throw new BusinessException(ErrorCode.UPSTREAM_ERROR);

        } catch (Exception e) {
            // 3. 예상치 못한 서버 내부 오류
            log.error("Uncaught exception during portal login for ID: {}", id, e);
            throw new BusinessException(ErrorCode.SERVER_ERROR);
        }

        // 4. 정보 추출 및 JWT 발급
        String major = info.getMajor();
        int grade = Integer.parseInt(info.getGrade().replaceAll("[^0-9]", "")); // 학년만 추출
        String accessToken = jwt.createAccessToken(id); // subject=학번 사용

        // 5. 응답 모델 구성
        UserMini user = UserMini.builder()
            .username(id.toString())
            .major(major)
            .grade(grade)
            .build();

        return LoginResult.builder()
            .accessToken(accessToken)
            .user(user)
            .build();
    }
}