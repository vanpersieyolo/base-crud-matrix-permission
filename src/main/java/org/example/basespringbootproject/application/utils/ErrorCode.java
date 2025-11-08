package org.example.basespringbootproject.application.utils;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    VALIDATION_ERROR("ERR_VALIDATION", HttpStatus.BAD_REQUEST),
    NOT_FOUND("ERR_NOT_FOUND", HttpStatus.NOT_FOUND),
    CONFLICT("ERR_CONFLICT", HttpStatus.CONFLICT),
    DATA_INTEGRITY("ERR_DATA_INTEGRITY", HttpStatus.CONFLICT),
    UNAUTHORIZED("ERR_UNAUTHORIZED", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("ERR_FORBIDDEN", HttpStatus.FORBIDDEN),
    INTERNAL("ERR_INTERNAL", HttpStatus.INTERNAL_SERVER_ERROR),
    BAD_REQUEST("BAD_REQUEST", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final HttpStatus status;

    ErrorCode(String code, HttpStatus status) {
        this.code = code;
        this.status = status;
    }

    public String code() {
        return code;
    }

    public HttpStatus status() {
        return status;
    }
}
