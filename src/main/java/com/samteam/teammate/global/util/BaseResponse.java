package com.samteam.teammate.global.util;

import org.springframework.http.HttpStatus;

import com.samteam.teammate.global.exception.docs.ErrorCode;

public record BaseResponse<T>(
	HttpStatus status,
	String message,
	T detail
) {

	public static BaseResponse<?> success(String message) {
		return new BaseResponse<>(HttpStatus.OK, message, null);
	}

	public static <T> BaseResponse<T> success(String message, T data) {
		return new BaseResponse<>(HttpStatus.OK, message, data);
	}

	public static BaseResponse<?> fail(ErrorCode e) {
		return new BaseResponse<>(e.getStatus(), e.getMessage(), e.getCode());
	}
}
