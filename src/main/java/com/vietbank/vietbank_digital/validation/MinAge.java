package com.vietbank.vietbank_digital.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Custom annotation để validate tuổi tối thiểu
 * Sử dụng: @MinAge(18) trên trường LocalDate
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MinAgeValidator.class)
@Documented
public @interface MinAge {
    String message() default "{validation.customer.age.min}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int value() default 18;
}
