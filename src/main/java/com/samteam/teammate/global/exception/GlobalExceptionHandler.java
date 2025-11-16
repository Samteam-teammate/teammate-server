package com.samteam.teammate.global.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.samteam.teammate.global.util.BaseResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	protected BaseResponse<?> handleBusinessException(BusinessException e) {
		return BaseResponse.fail(e.getErrorCode());
	}
}
