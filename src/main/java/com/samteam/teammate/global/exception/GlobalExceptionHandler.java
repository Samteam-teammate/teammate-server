package com.samteam.teammate.global.exception;

import com.samteam.teammate.global.exception.docs.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.samteam.teammate.global.util.BaseResponse;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	protected BaseResponse<?> handleBusinessException(BusinessException e) {
		return BaseResponse.fail(e.getErrorCode());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected BaseResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		return BaseResponse.fail(ErrorCode.INVALID_INPUT);
	}


    @ExceptionHandler(Exception.class)
    protected BaseResponse<?> handleAllExceptions(Exception e) {
        log.error("Unhandled Internal Server Error occurred:", e);

        return BaseResponse.fail(ErrorCode.SERVER_ERROR);
    }
}
