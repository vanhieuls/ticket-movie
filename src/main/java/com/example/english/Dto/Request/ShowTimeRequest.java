package com.example.english.Dto.Request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShowTimeRequest {
    @JsonFormat(pattern = "dd-MM-yyyy")
    LocalDate showDate;
    @JsonFormat(pattern = "HH:mm")
    LocalTime startTime;
    @JsonFormat(pattern = "HH:mm")
    LocalTime endTime;
    Long screenRoomId;
    Long movieId;
}
