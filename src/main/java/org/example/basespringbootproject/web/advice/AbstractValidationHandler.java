// language: java
package org.example.basespringbootproject.web.advice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.example.basespringbootproject.application.utils.ErrorCode;
import org.example.basespringbootproject.infrastructure.exception.ApiError;
import org.example.basespringbootproject.infrastructure.exception.BaseException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Reusable abstract validation helper.
 * - Use {@code validateOrThrow(dto)} to validate beans annotated with Jakarta validation.
 * - Use {@code validateMapOrThrow(...)} to validate dynamic filter/maps against a simple schema.
 * <p>
 * Concrete controllers/services can extend this class and call the protected helpers.
 */
public abstract class AbstractValidationHandler {

    protected final Validator validator;

    protected AbstractValidationHandler(Validator validator) {
        this.validator = Objects.requireNonNull(validator);
    }

    /**
     * Validate a bean using jakarta.validation.Validator.
     * Throws BaseException with ErrorCode.VALIDATION_ERROR when violations exist.
     */
    protected <T> void validateOrThrow(T dto) {
        if (dto == null) return;
        Set<ConstraintViolation<T>> violations = validator.validate(dto);
        if (!violations.isEmpty()) {
            List<ApiError.FieldError> errors = violations.stream()
                    .map(this::toFieldError)
                    .collect(Collectors.toList());
            throw BaseException.of(ErrorCode.VALIDATION_ERROR, "Validation failed", errors);
        }
    }

    /**
     * Validate a Map (e.g. dynamic filters) against a provided schema.
     *
     * @param values           map to validate
     * @param schema           map of field -> expected type (Class). If a value is present but not instanceOf expected type, error recorded.
     * @param requiredFields   set of required keys (can be empty)
     * @param customValidators map of field -> predicate that returns true if valid (optional)
     */
    protected void validateMapOrThrow(Map<String, ?> values,
                                      Map<String, Class<?>> schema,
                                      Set<String> requiredFields,
                                      Map<String, Predicate<Object>> customValidators) {
        List<ApiError.FieldError> errors = new ArrayList<>();
        if (requiredFields != null) {
            for (String req : requiredFields) {
                if (values == null || !values.containsKey(req) || values.get(req) == null) {
                    errors.add(new ApiError.FieldError(req, null, "Required"));
                }
            }
        }

        if (values != null && schema != null) {
            for (Map.Entry<String, Class<?>> entry : schema.entrySet()) {
                String key = entry.getKey();
                Class<?> expected = entry.getValue();
                if (!values.containsKey(key) || values.get(key) == null) continue;
                Object val = values.get(key);
                if (expected != null && !expected.isInstance(val)) {
                    // allow simple conversions for common primitives passed as String
                    if (!(val instanceof String) || !isConvertibleString((String) val, expected)) {
                        errors.add(new ApiError.FieldError(key, String.valueOf(val), "Expected type " + expected.getSimpleName()));
                        continue;
                    }
                }
                if (customValidators != null && customValidators.containsKey(key)) {
                    Predicate<Object> pred = customValidators.get(key);
                    try {
                        if (!pred.test(val)) {
                            errors.add(new ApiError.FieldError(key, String.valueOf(val), "Custom validation failed"));
                        }
                    } catch (Exception ex) {
                        errors.add(new ApiError.FieldError(key, String.valueOf(val), "Validation error: " + ex.getMessage()));
                    }
                }
            }
        }

        if (!errors.isEmpty()) {
            throw BaseException.of(ErrorCode.VALIDATION_ERROR, "Validation failed", errors);
        }
    }

    // --- helpers ---

    protected <T> ApiError.FieldError toFieldError(ConstraintViolation<T> cv) {
        String path = cv.getPropertyPath() == null ? null : cv.getPropertyPath().toString();
        Object invalid = cv.getInvalidValue();
        String invalidStr = invalid == null ? null : invalid.toString();
        return new ApiError.FieldError(path, invalidStr, cv.getMessage());
    }

    private boolean isConvertibleString(String value, Class<?> expected) {
        try {
            if (expected == Integer.class || expected == int.class) {
                Integer.parseInt(value);
                return true;
            }
            if (expected == Long.class || expected == long.class) {
                Long.parseLong(value);
                return true;
            }
            if (expected == Double.class || expected == double.class) {
                Double.parseDouble(value);
                return true;
            }
            if (expected == Boolean.class || expected == boolean.class) {
                String v = value.toLowerCase(Locale.ROOT);
                return "true".equals(v) || "false".equals(v) || "1".equals(v) || "0".equals(v);
            }
            // allow strings for String.class
            if (expected == String.class) return true;
        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * Convenience: validate and wrap Jakarta ConstraintViolationException into BaseException for uniform handling.
     */
    protected void handleConstraintViolationException(ConstraintViolationException ex) {
        List<ApiError.FieldError> errors = ex.getConstraintViolations().stream()
                .map(this::toFieldError)
                .collect(Collectors.toList());
        throw BaseException.of(ErrorCode.VALIDATION_ERROR, "Validation failed", errors);
    }
}