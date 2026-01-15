package com.example.english.Dto.Request;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatRequest {
    Long screenRoomId;
    @Size(min = 2, message = "number of rows must be greater than or equal 2")
    @Size(max = 26, message = "number of rows must be less than or equal 26")
    int rows;
    @Size(min = 5, message = "number of columns must be greater than or equal 5")
    @Size(max = 26, message = "number of columns must be less than or equal 26")
    int columns;
    Long[][] seatTypeId;
}
