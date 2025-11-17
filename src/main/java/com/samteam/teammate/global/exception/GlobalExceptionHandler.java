package com.samteam.teammate.global.exception;

import com.samteam.teammate.global.exception.docs.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.samteam.teammate.global.util.BaseResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	protected BaseResponse<?> handleBusinessException(BusinessException e) {
		return BaseResponse.fail(e.getErrorCode());
	}

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    protected BaseResponse<?> handleAllExceptions(Exception e) {
        // 🔴 여기서 스택 트레이스가 콘솔에 출력됩니다.
        log.error("Unhandled Internal Server Error occurred:", e);

        // 클라이언트에게는 정의된 500 ErrorCode를 반환
        return BaseResponse.fail(ErrorCode.SERVER_ERROR);
        // (참고: ErrorCode.java에 SERVER_ERROR를 추가해야 합니다. 아래 3단계 참조)
    }
}
