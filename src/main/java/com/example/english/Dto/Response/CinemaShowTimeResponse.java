package com.example.english.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CinemaShowTimeResponse {
    Long cinemaId;
    String cinemaName;
    List<ShowTimeSummaryResponse> showTimeSummaryResponseList;
    @Data
    public static class ShowTimeSummaryResponse{
        private Long showTimeId;
        private LocalTime startTime;
        private LocalTime endTime;
    }
}
