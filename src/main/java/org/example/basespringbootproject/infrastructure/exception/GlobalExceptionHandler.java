package org.example.basespringbootproject.infrastructure.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.example.basespringbootproject.application.utils.ErrorCode;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ApiError> handleBase(BaseException ex, HttpServletRequest request) {
        ApiError body = new ApiError(
                ex.getStatus().value(),
                ex.getStatus().getReasonPhrase(),
                ex.getCode(),
                ex.getMessage(),
                ex.getDetails(),
                null,
                request.getRequestURI()
        );
        return ResponseEntity.status(ex.getStatus()).body(body);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        List<ApiError.FieldError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(this::toFieldError)
                .collect(Collectors.toList());

        ApiError body = new ApiError(
                ErrorCode.VALIDATION_ERROR.status().value(),
                ErrorCode.VALIDATION_ERROR.status().getReasonPhrase(),
                ErrorCode.VALIDATION_ERROR.code(),
                "Validation failed",
                null,
                errors,
                request.getRequestURI()
        );
        return ResponseEntity.status(ErrorCode.VALIDATION_ERROR.status()).body(body);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        ApiError body = new ApiError(
                ErrorCode.NOT_FOUND.status().value(),
                ErrorCode.NOT_FOUND.status().getReasonPhrase(),
                ErrorCode.NOT_FOUND.code(),
                ex.getMessage(),
                null,
                null,
                request.getRequestURI()
        );
        return ResponseEntity.status(ErrorCode.NOT_FOUND.status()).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiError> handleDataIntegrity(DataIntegrityViolationException ex, HttpServletRequest request) {
        ex.getMostSpecificCause();
        ApiError body = new ApiError(
                ErrorCode.DATA_INTEGRITY.status().value(),
                ErrorCode.DATA_INTEGRITY.status().getReasonPhrase(),
                ErrorCode.DATA_INTEGRITY.code(),
                "Data integrity violation",
                ex.getMostSpecificCause().getMessage(),
                null,
                request.getRequestURI()
        );
        return ResponseEntity.status(ErrorCode.DATA_INTEGRITY.status()).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneric(Exception ex, HttpServletRequest request) {
        ApiError body = new ApiError(
                ErrorCode.INTERNAL.status().value(),
                ErrorCode.INTERNAL.status().getReasonPhrase(),
                ErrorCode.INTERNAL.code(),
                "Unexpected error",
                ex.getMessage(),
                null,
                request.getRequestURI()
        );
        return ResponseEntity.status(ErrorCode.INTERNAL.status()).body(body);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
        ApiError body = new ApiError(
                ErrorCode.BAD_REQUEST.status().value(),
                ErrorCode.BAD_REQUEST.status().getReasonPhrase(),
                ErrorCode.BAD_REQUEST.code(),
                ex.getMessage(),
                null,
                null,
                request.getRequestURI()
        );
        return ResponseEntity.status(ErrorCode.BAD_REQUEST.status()).body(body);
    }

    private ApiError.FieldError toFieldError(FieldError fe) {
        Object rejected = fe.getRejectedValue();
        String rejectedStr = rejected == null ? null : rejected.toString();
        return new ApiError.FieldError(fe.getField(), rejectedStr, fe.getDefaultMessage());
    }
}