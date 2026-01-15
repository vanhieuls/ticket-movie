package com.example.english.Dto.Request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketPriceRequest {
    Long cinemaTypeId;
    Long screenRoomTypeId;
    Long seatTypeId;
    String timeFrame;
    String dayType;
}
