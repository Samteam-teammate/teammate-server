package com.samteam.teammate.global.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.samteam.teammate.global.exception.docs.ErrorCode;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.samteam.teammate.global.util.BaseResponse;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	protected BaseResponse<?> handleBusinessException(BusinessException e) {
        return BaseResponse.fail(e.getErrorCode());
	}

    // Bean Validation 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected BaseResponse<?> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        var fieldErrors = e.getBindingResult().getFieldErrors().stream()
            .map(fe -> java.util.Map.of(
                "field", fe.getField(),
                "rejectedValue", String.valueOf(fe.getRejectedValue()),
                "reason", Objects.toString(fe.getDefaultMessage(), "")
            ))
            .toList();

        var logLines = fieldErrors.stream()
            .map(fe -> String.format(
                "%s=%s (%s)",
                fe.get("field"),
                fe.get("rejectedValue"),
                fe.get("reason")
            ))
            .toList();

        log.warn("MethodArgumentNotValidException: {}", logLines);

        var detail = java.util.Map.of(
            "code", ErrorCode.INVALID_INPUT.getCode(),
            "fields", fieldErrors
        );

        return BaseResponse.fail(ErrorCode.INVALID_INPUT, detail);
    }

    // 필수 파라미터 누락
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected BaseResponse<?> handleMissingServletRequestParameter(MissingServletRequestParameterException e) {
        log.warn("MissingServletRequestParameterException: param={}", e.getParameterName());
        return BaseResponse.fail(ErrorCode.INVALID_INPUT);
    }

    // JSON 파싱/타입 불일치, 쿼리 파라미터 타입 불일치
    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        MethodArgumentTypeMismatchException.class,
        InvalidFormatException.class // 혹시 직접 던져지는 경우 대비
    })
    protected BaseResponse<?> handleTypeMismatch(Exception e) {

        // 1) JSON body 쪽 (주로 HttpMessageNotReadableException → InvalidFormatException)
        if (e instanceof HttpMessageNotReadableException hmr) {
            Throwable cause = hmr.getMostSpecificCause();

            if (cause instanceof InvalidFormatException ife) {
                logInvalidFormat(ife);
            } else {
                log.warn("Unreadable request body: {}", hmr.getMessage());
            }
        }
        // 2) enum 등에서 InvalidFormatException이 직접 잡히는 경우
        else if (e instanceof InvalidFormatException ife) {
            logInvalidFormat(ife);
        }
        // 3) 쿼리 파라미터, path variable 타입 불일치
        else if (e instanceof MethodArgumentTypeMismatchException matme) {
            log.warn("MethodArgumentTypeMismatchException: param={}, value={}, requiredType={}",
                matme.getName(),
                matme.getValue(),
                matme.getRequiredType() != null ? matme.getRequiredType().getSimpleName() : null
            );
        } else {
            log.warn("Type mismatch or unreadable request body: {}", e.getMessage());
        }

        // 클라이언트 응답은 일단 공통 400으로 통일
        return BaseResponse.fail(ErrorCode.INVALID_INPUT);
    }

    @ExceptionHandler(AccessDeniedException.class)
	public BaseResponse<?> handleAccessDeniedException(AccessDeniedException e) {
		return BaseResponse.fail(ErrorCode.FORBIDDEN_ERROR);
	}

    @ExceptionHandler(Exception.class)
    protected BaseResponse<?> handleAllExceptions(Exception e) {
        log.error("Unhandled Internal Server Error occurred:", e);

        return BaseResponse.fail(ErrorCode.SERVER_ERROR);
    }


    /**
     * Jackson InvalidFormatException 상세 로그
     * - 어느 field에서
     * - 어떤 값이
     * - 어떤 타입/enum과 안 맞는지
     */
    private void logInvalidFormat(InvalidFormatException ife) {
        String fieldPath = ife.getPath().stream()
            .map(ref -> {
                if (ref.getFieldName() != null) {
                    return ref.getFieldName();
                } else {
                    // 배열 인덱스일 수 있음
                    return "[" + ref.getIndex() + "]";
                }
            })
            .collect(Collectors.joining("."));

        Object invalidValue = ife.getValue();
        Class<?> targetType = ife.getTargetType();

        if (targetType != null && targetType.isEnum()) {
            Object[] allowed = targetType.getEnumConstants();
            log.warn(
                "Invalid enum value: field={}, value={}, allowed={}",
                fieldPath, invalidValue, allowed
            );
        } else {
            log.warn(
                "Invalid format: field={}, value={}, targetType={}",
                fieldPath, invalidValue,
                targetType != null ? targetType.getSimpleName() : null
            );
        }
    }
}
