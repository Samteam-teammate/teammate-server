package com.samteam.teammate.global.util;

import org.springframework.http.HttpStatus;

public record BasicResponse(
	HttpStatus status,
	String message,
	String detail
) {
	public static BasicResponse of(String message) {
		return new BasicResponse(
			HttpStatus.OK,
			message,
			""
		);
	}

	/*public static BasicResponse of(BusinessException exception) {
		return new BasicResponse(
			exception.getStatus(),
			exception.getMessageCode(),
			exception.getDetail()
		);
	}

	public static ResponseEntity<BasicResponse> to(BusinessException exception) {
		return ResponseEntity
			.status(exception.getStatus())
			.body(BasicResponse.of(exception));
	}

	public static BasicResponse from(ErrorCode errorCode, MessageUtil messageUtil) {
		return new BasicResponse(errorCode.getStatus(),
			messageUtil.resolveMessage(errorCode.getMessageKey()),
			"");
	}*/
}
