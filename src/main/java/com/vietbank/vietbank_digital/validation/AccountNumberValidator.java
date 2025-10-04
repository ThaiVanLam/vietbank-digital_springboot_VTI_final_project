package com.vietbank.vietbank_digital.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator cho số tài khoản
 */
public class AccountNumberValidator implements ConstraintValidator<ValidAccountNumber, String> {
    @Override
    public boolean isValid(String accountNumber, ConstraintValidatorContext context) {
        if (accountNumber == null || accountNumber.trim().isEmpty()) {
            return true;
        }

        // Kiểm tra format: chữ số, độ dài 10-16 ký tự
        return accountNumber.matches("^[0-9]{10,16}$");
    }
}
