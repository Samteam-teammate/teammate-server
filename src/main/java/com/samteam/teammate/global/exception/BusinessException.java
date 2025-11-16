package com.samteam.teammate.global.exception;

import com.samteam.teammate.global.exception.docs.ErrorCode;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException{

	private final ErrorCode errorCode;

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
