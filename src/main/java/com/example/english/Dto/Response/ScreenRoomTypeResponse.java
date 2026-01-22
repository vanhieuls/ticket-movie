package com.example.english.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScreenRoomTypeResponse {
    Long id;
    Double priceFactor;
    String name;
    String status;
}
