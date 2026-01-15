package com.example.english.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Getter
@Setter
public class ShowTimeResponse {
    Long id;
    LocalDate showDate;
    LocalTime startTime;
    LocalTime endTime;
    boolean status;
    Long screenRoomId;
    Long movieId;
}
