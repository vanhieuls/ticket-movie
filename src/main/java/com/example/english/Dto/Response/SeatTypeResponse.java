package com.example.english.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class SeatTypeResponse {
    Long id;
    String name;
    Double priceFactor;
}
