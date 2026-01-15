package com.example.english.Enum;

import jakarta.validation.Payload;

public @interface EnumSubset {
    // Danh sách constant được phép
    String[] anyOf();

    String message() default "must be any of {anyOf}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
