package com.vietbank.vietbank_digital.config.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception khi số dư không đủ
 */
public class InsufficientBalanceException extends BusinessException {
    public InsufficientBalanceException() {
        super("error.account.insufficientBalance", HttpStatus.BAD_REQUEST);
    }
}
