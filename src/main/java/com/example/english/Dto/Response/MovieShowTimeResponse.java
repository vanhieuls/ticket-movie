package com.example.english.Dto.Response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieShowTimeResponse {
    Long movieId;
    String movieName;
    String posterUrl;
    List<ShowTimeSummaryResponse> showTimeSummaryResponseList;
    @Data
    public static class ShowTimeSummaryResponse{
        private Long showTimeId;
        private LocalTime startTime;
        private LocalTime endTime;
    }
}

