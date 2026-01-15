package com.example.english.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SeatShowTime {
    Long showTimeId;
    String CinemaName;
    String MovieName;
    int sumSeats;
    List<SeatShowTimeResponse> seatList;
}
