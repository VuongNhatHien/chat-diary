package com.hcmus.exception;

import com.google.api.client.http.HttpStatusCodes;
import com.hcmus.dto.response.ApiResponse;
import com.hcmus.dto.response.ErrorCodes;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationRuntimeException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequestException(
        ApplicationRuntimeException exception) {
        ApiResponse<Object> response = ApiResponse.error(exception.getData(), exception.getStatus(),
            exception.getCode());
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<Void> handleAuthorizationDeniedException(
        AuthorizationDeniedException exception) {
        return ApiResponse.error(HttpStatusCodes.STATUS_CODE_FORBIDDEN, ErrorCodes.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleSecurityException(Exception exception) {
        log.error(exception.getMessage(), exception);

        return ApiResponse.error(HttpStatusCodes.STATUS_CODE_SERVER_ERROR,
            ErrorCodes.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse<Void> handleExpiredJwtException(ExpiredJwtException exception) {
        return ApiResponse.error(HttpStatusCodes.STATUS_CODE_UNAUTHORIZED,
            ErrorCodes.JWT_EXPIRED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Map<String, String>> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ApiResponse.error(errors, HttpStatusCodes.STATUS_CODE_BAD_REQUEST,
            ErrorCodes.VALIDATION_ERROR);
    }


}
