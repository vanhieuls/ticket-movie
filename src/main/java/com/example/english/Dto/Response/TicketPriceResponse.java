package com.example.english.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TicketPriceResponse {
    Long id;
    String timeFrame;
    String dayType;
    String cinemaType;
    String screenRoomType;
    String seatType;
    BigDecimal price;
}
