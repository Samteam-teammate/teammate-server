package com.samteam.teammate.global.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.samteam.teammate.global.exception.docs.ErrorCode;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

        List<Map<String, String>> fieldErrors = e.getBindingResult().getFieldErrors().stream()
            .map(fe -> java.util.Map.of(
                "field", fe.getField(),
                "rejectedValue", String.valueOf(fe.getRejectedValue()),
                "reason", Objects.toString(fe.getDefaultMessage(), "")
            ))
            .toList();

        List<String> logLines = fieldErrors.stream()
            .map(fe -> String.format(
                "%s=%s (%s)",
                fe.get("field"),
                fe.get("rejectedValue"),
                fe.get("reason")
            ))
            .toList();

        log.warn("MethodArgumentNotValidException: {}", logLines);

        Map<String, Object> detail = Map.of(
            "code", ErrorCode.INVALID_INPUT.getCode(),
            "detail", fieldErrors
        );

        return BaseResponse.fail(ErrorCode.INVALID_INPUT, detail);
    }

    // 필수 파라미터 누락
    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected BaseResponse<?> handleMissingServletRequestParameter(MissingServletRequestParameterException e) {
        log.warn("MissingServletRequestParameterException: param={}", e.getParameterName());
        Map<String, String> detail = Map.of(
            "code", ErrorCode.INVALID_INPUT.getCode(),
            "detail", e.getParameterName() + "is missing"
        );
        return BaseResponse.fail(ErrorCode.INVALID_INPUT, detail);
    }

    // JSON 파싱/타입 불일치, 쿼리 파라미터 타입 불일치
    @ExceptionHandler({
        HttpMessageNotReadableException.class,
        MethodArgumentTypeMismatchException.class,
        InvalidFormatException.class
    })
    protected BaseResponse<?> handleTypeMismatch(Exception e) {
        if (e == null) log.warn("Type Mismatch Error is Empty");

        String detail = "Type Mismatch";

		switch (e) {
            // 1) JSON body 쪽 (주로 HttpMessageNotReadableException → InvalidFormatException)
			case HttpMessageNotReadableException hmr -> {
				Throwable cause = hmr.getMostSpecificCause();
				if (cause instanceof InvalidFormatException ife) {
					detail = logInvalidFormat(ife);
				} else {
					log.warn("Unreadable request body: {}", hmr.getMessage());
				}
			}

			// 2) enum 등에서 InvalidFormatException이 직접 잡히는 경우
			case InvalidFormatException ife -> detail = logInvalidFormat(ife);

			// 3) 쿼리 파라미터, path variable 타입 불일치
			case MethodArgumentTypeMismatchException matme -> {
                detail = "param=" + matme.getName() + ", value=" + matme.getValue() + ", requiredType=" + matme.getRequiredType().getSimpleName();
                log.warn("MethodArgumentTypeMismatchException: " + detail);
            }

            case null -> log.warn("Type mismatch or unreadable request body");
            default -> log.warn("Type mismatch or unreadable request body: {}", e.getMessage());
		}

        return BaseResponse.fail(ErrorCode.INVALID_INPUT, detail);
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
    private String logInvalidFormat(InvalidFormatException ife) {
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

        String logMsg;

        if (targetType != null && targetType.isEnum()) {
            Object[] allowed = targetType.getEnumConstants();
            logMsg = "field=" + fieldPath+ " value=" + invalidValue + ", allowed={}" + Arrays.toString(allowed);
            log.warn("Invalid enum value: " + logMsg);
        } else {
            logMsg = "field=" + fieldPath+ " value=" + invalidValue + ", allowed={}" + (targetType != null ? targetType.getSimpleName() : "");
            log.warn("Invalid enum value: " + logMsg);
        }

        return logMsg;
    }
}
