package com.vietbank.vietbank_digital.config.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception khi không có quyền
 */
public class UnauthorizedException extends BusinessException {
    public UnauthorizedException(String messageKey, Object... args) {
        super(messageKey, HttpStatus.FORBIDDEN, args);
    }
}
