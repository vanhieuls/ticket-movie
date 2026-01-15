package com.example.english.Enum;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum CinemaType {
    NORMAL("Normal", 1.0),
    PREMIUM("IMAX", 1.1),
    LUXURY("3D", 1.2);
    String name;
    Double priceFactor;
}
