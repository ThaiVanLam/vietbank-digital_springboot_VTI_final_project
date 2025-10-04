package com.vietbank.vietbank_digital.config.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception khi không tìm thấy resource
 */
public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String messageKey, Object... args) {
        super(messageKey, HttpStatus.NOT_FOUND, args);
    }
}
