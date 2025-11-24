package com.samteam.teammate.global.exception.docs;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // 400 Bad Request
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "E400", "입력값이 올바르지 않습니다"),	// 공통 400

    // 401 Unauthorized
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "J001", "토큰이 유효하지 않습니다"),
	UNAUTHORIZED_ERROR(HttpStatus.UNAUTHORIZED, "E401", "사용자 인증에 실패했습니다"),	// 공통 401
    SJU_AUTH_FAILED(HttpStatus.UNAUTHORIZED, "A001", "학번/비밀번호 인증에 실패했습니다"),	// Auth

	// 403 Forbidden
	FORBIDDEN_ERROR(HttpStatus.FORBIDDEN, "E403", "접근 권한이 없습니다"),	// 공통 403

    // 404 Not Found
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "M001", "사용자가 존재하지 않습니다"),	// Member

    // 409 Conflict
    MEMBER_ALREADY_EXIST(HttpStatus.CONFLICT, "M002", "사용자가 이미 존재합니다"), // Member

    // 502 Bad Gateway (Upstream Error)
    UPSTREAM_ERROR(HttpStatus.BAD_GATEWAY, "U001", "외부 시스템 연결에 실패했습니다"),	// Upstream

    // 500 Internal Server Error (예상치 못한 오류)
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E500", "서버 처리 중 예상치 못한 오류가 발생했습니다");	// 공통 500


	private final HttpStatus status;
	private final String code;
	private final String message;
}
