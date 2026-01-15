package com.example.english.Enum;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class EnumSubsetValidator implements ConstraintValidator<EnumSubset, Enum<?>> {

    private Set<String> acceptedValues;

    @Override
    public void initialize(EnumSubset constraintAnnotation) {
        acceptedValues = new HashSet<>(Arrays.asList(constraintAnnotation.anyOf()));
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) return true;
        return acceptedValues.contains(value.name());
    }
}
