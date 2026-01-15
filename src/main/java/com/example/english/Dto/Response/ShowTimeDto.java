package com.example.english.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShowTimeDto {
    Long id;
    LocalDate showDate;
    LocalTime startTime;
    LocalTime endTime;
    Long screenRoomId;
    String movieName;
}
