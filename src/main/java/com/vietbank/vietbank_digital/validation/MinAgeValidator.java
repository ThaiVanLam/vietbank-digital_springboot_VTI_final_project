package com.vietbank.vietbank_digital.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.Period;

/**
 * Validator implementation cho @MinAge
 */
public class MinAgeValidator implements ConstraintValidator<MinAge, LocalDate> {
    private int minAge;

    @Override
    public void initialize(MinAge constraintAnnotation) {
        this.minAge = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(LocalDate dateOfBirth, ConstraintValidatorContext context) {
        if (dateOfBirth == null) {
            return true; // null values handled by @NotNull
        }

        LocalDate now = LocalDate.now();
        int age = Period.between(dateOfBirth, now).getYears();

        return age >= minAge;
    }
}
