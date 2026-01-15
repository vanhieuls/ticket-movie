package com.example.english.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatResponse {
    Long id;
    Long screenRoomId;
    Long seatTypeId;
    String seatCode;
    int row;
    int column;
}
