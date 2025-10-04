package com.vietbank.vietbank_digital.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Base exception cho business logic
 */
@Getter
public class BusinessException extends RuntimeException{
    private final String messageKey;
    private final Object[] args;
    private final HttpStatus httpStatus;

    public BusinessException(String messageKey, HttpStatus httpStatus, Object... args) {
        super(messageKey);
        this.messageKey = messageKey;
        this.args = args;
        this.httpStatus = httpStatus;
    }



    /**
     * Exception khi dữ liệu đã tồn tại
     */
    class DuplicateResourceException extends BusinessException {
        public DuplicateResourceException(String messageKey, Object... args) {
            super(messageKey, HttpStatus.CONFLICT, args);
        }
    }

    /**
     * Exception khi giao dịch thất bại
     */
    class TransactionFailedException extends BusinessException {
        public TransactionFailedException(String messageKey, Object... args) {
            super(messageKey, HttpStatus.BAD_REQUEST, args);
        }
    }

    /**
     * Exception khi không có quyền
     */
    class UnauthorizedException extends BusinessException {
        public UnauthorizedException(String messageKey, Object... args) {
            super(messageKey, HttpStatus.FORBIDDEN, args);
        }
    }

    /**
     * Exception khi số dư không đủ
     */
    class InsufficientBalanceException extends BusinessException {
        public InsufficientBalanceException() {
            super("error.account.insufficientBalance", HttpStatus.BAD_REQUEST);
        }
    }
}
