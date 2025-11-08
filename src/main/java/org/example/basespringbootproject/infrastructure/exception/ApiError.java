package org.example.basespringbootproject.infrastructure.exception;

import lombok.Getter;

import java.time.OffsetDateTime;
import java.util.List;

/**
 * Simple DTO returned to clients on errors.
 */
@Getter
public class ApiError {
    private final OffsetDateTime timestamp = OffsetDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;
    private final Object details;
    private final List<FieldError> errors;
    private final String path;

    public ApiError(int status, String error, String code, String message, Object details, List<FieldError> errors, String path) {
        this.status = status;
        this.error = error;
        this.code = code;
        this.message = message;
        this.details = details;
        this.errors = errors;
        this.path = path;
    }

    public static record FieldError(String field, String rejectedValue, String message) {}
}