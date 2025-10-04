package com.vietbank.vietbank_digital.config.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception khi giao dịch thất bại
 */
public class TransactionFailedException extends BusinessException {
    public TransactionFailedException(String messageKey, Object... args) {
        super(messageKey, HttpStatus.BAD_REQUEST, args);
    }
}
