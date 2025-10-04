package com.vietbank.vietbank_digital.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom annotation để validate số tài khoản ngân hàng
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AccountNumberValidator.class)
@Documented
public @interface ValidAccountNumber {
    String message() default "{validation.account.number.invalid}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
