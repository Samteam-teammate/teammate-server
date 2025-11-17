package com.samteam.teammate.domain.auth.api;

import com.samteam.teammate.domain.auth.dto.LoginResult;
import com.samteam.teammate.domain.auth.service.PortalAuthService;
import com.samteam.teammate.global.exception.BusinessException;
import com.samteam.teammate.global.exception.docs.ErrorCode;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PortalAuthApi {

    private final PortalAuthService svc;

    @PostMapping("/login")
    public ResponseEntity<LoginResult> login(@RequestBody LoginPayload req, HttpServletResponse response) {

        // 1. 입력값 유효성 검증
        if (req.getId() == null || req.getId().isBlank() || req.getPw() == null || req.getPw().isBlank()) {
            // 400 Bad Request 처리
            throw new BusinessException(ErrorCode.INVALID_INPUT);
        }

        // 2. 서비스 호출 (모든 예외는 Service에서 BusinessException으로 변환)
        LoginResult result = svc.login(Long.valueOf(req.getId().trim()), req.getPw(), response);

        return ResponseEntity.ok(result);
    }

    // API 레벨의 @ExceptionHandler는 모두 GlobalExceptionHandler로 위임했기 때문에 이 파일에서는 제거합니다.
}