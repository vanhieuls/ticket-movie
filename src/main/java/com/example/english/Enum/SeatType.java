package com.example.english.Enum;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public enum SeatType {
    NORMAL("STANDARD", 1.0),
    PREMIUM("VIP", 1.02),
    LUXURY("SWEET BOX", 1.05);

    private final String name;
    private final Double priceFactor;

}
