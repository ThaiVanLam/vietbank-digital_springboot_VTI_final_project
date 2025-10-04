package com.vietbank.vietbank_digital.config.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception khi dữ liệu đã tồn tại
 */
public class DuplicateResourceException extends BusinessException {
    public DuplicateResourceException(String messageKey, Object... args) {
        super(messageKey, HttpStatus.CONFLICT, args);
    }
}
