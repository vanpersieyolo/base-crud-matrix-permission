package org.example.basespringbootproject.infrastructure.exception;

import lombok.Getter;
import org.example.basespringbootproject.application.utils.ErrorCode;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private final String code;
    private final HttpStatus status;
    private final Object details;

    public BaseException(String code, String message, HttpStatus status) {
        this(code, message, status, null);
    }

    public BaseException(String code, String message, HttpStatus status, Object details) {
        super(message);
        this.code = code;
        this.status = status == null ? HttpStatus.INTERNAL_SERVER_ERROR : status;
        this.details = details;
    }

    public static BaseException of(ErrorCode errorCode, String message) {
        return new BaseException(errorCode.code(), message, errorCode.status());
    }

    public static BaseException of(ErrorCode errorCode, String message, Object details) {
        return new BaseException(errorCode.code(), message, errorCode.status(), details);
    }
}